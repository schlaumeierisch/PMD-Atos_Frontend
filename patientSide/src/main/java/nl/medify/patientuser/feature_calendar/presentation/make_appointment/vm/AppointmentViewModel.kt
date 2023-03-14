package nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.patientuser.feature_calendar.domain.use_case.CalendarUseCases
import nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.states.AllDoctorsAndCareProvidersOfPatientState
import nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.states.AvailableTimeSlotsOfParticularDayState
import nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.states.GeoCodesState
import nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.states.NotAvailableDaysInThisMonthState
import nl.medify.utilities.util.Resource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    var chosenDateTime: LocalDateTime? = null; private set

    var chosenTime: LocalTime? = null; private set
    var reasonOfVisit: String? = null; private set
    var selectedDoctorOrCareProvider: DoctorOrCareProvider? = null; private set
    var chosenDate: LocalDate? = null; private set
    var dateToday: LocalDate = LocalDate.now()

    var appointmentSuccessfullyCreated: Boolean? = null

    fun saveDate(date: LocalDate) { chosenDate = date }
    fun saveTime(time: LocalTime) { chosenTime = time; chosenDateTime = chosenTime?.atDate(chosenDate) }
    fun saveReasonOfVisit(reason: String) { reasonOfVisit = reason }
    fun saveSelectedDoctorOrCareProviderID(doctorOrCareProvider: DoctorOrCareProvider) { selectedDoctorOrCareProvider = doctorOrCareProvider }

    private val _allDoctorAndCareProvidersOfPatient = MutableStateFlow(AllDoctorsAndCareProvidersOfPatientState())
    var allDoctorAndCareProvidersOfPatient: StateFlow<AllDoctorsAndCareProvidersOfPatientState> = _allDoctorAndCareProvidersOfPatient

    private val _notAvailableDaysList = MutableStateFlow(NotAvailableDaysInThisMonthState())
    var notAvailableDaysList: StateFlow<NotAvailableDaysInThisMonthState> = _notAvailableDaysList

    private val _availableTimeSlots = MutableStateFlow(AvailableTimeSlotsOfParticularDayState())
    var availableTimeSlots: StateFlow<AvailableTimeSlotsOfParticularDayState> = _availableTimeSlots

    private val _geoCodes = MutableStateFlow(GeoCodesState())
    var geoCodes: StateFlow<GeoCodesState> = _geoCodes

    fun getAllDoctorsAndCareProvidersOfPatient(id: String) = viewModelScope.launch(Dispatchers.IO) {
        calendarUseCases.getAllDoctorsAndCareProvidersOfPatientUseCase.invoke(id).collect {
            when (it) {
                is Resource.Success -> {
                    _allDoctorAndCareProvidersOfPatient.value = AllDoctorsAndCareProvidersOfPatientState(allDoctorsAndCareProvidersOfPatient = it.data)
                }
                is Resource.Loading -> {
                    _allDoctorAndCareProvidersOfPatient.value = AllDoctorsAndCareProvidersOfPatientState(isLoading = true)
                }
                is Resource.Error -> {
                    _allDoctorAndCareProvidersOfPatient.value = AllDoctorsAndCareProvidersOfPatientState(error = it.message ?: "Unexpected error occured.")
                }
                is Resource.Empty -> {
                    _allDoctorAndCareProvidersOfPatient.value = AllDoctorsAndCareProvidersOfPatientState(allDoctorsAndCareProvidersOfPatient = ArrayList())
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }

    fun postCreateAppointment(patientID: String) = viewModelScope.launch(Dispatchers.IO) {
        if (selectedDoctorOrCareProvider?.id?.startsWith("dom-id-gp") == true) {
            calendarUseCases.postCreateAppointmentUseCase.invoke(chosenDateTime!!, reasonOfVisit!!, patientID, selectedDoctorOrCareProvider?.id, null)
        } else {
            calendarUseCases.postCreateAppointmentUseCase.invoke(chosenDateTime!!, reasonOfVisit!!, patientID, null, selectedDoctorOrCareProvider?.id)
        }
    }

    fun getGeoCodes(city: String, street: String, postalcode: String) = viewModelScope.launch(Dispatchers.IO) {
        calendarUseCases.getGeoCodesUseCase.invoke(city, street, postalcode).collect {
            when (it) {
                is Resource.Success -> {
                    _geoCodes.value = GeoCodesState(geoCode = LatLng(it.data?.get(0)?.lat?.toDouble() ?: 0.0, it.data?.get(0)?.lon?.toDouble() ?: 0.0))
                }
                is Resource.Loading -> {
                    _geoCodes.value = GeoCodesState(isLoading = true)
                }
                is Resource.Error -> {
                    _geoCodes.value = GeoCodesState(error = it.message ?: "Unexpected error occured.")
                }
                is Resource.Empty -> {
                    _geoCodes.value = GeoCodesState(geoCode = LatLng(0.0, 0.0))
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }

    fun getNotAvailableDaysInThisMonth(yearNumber: Int, monthNumber: Int, gpId: String) = viewModelScope.launch(Dispatchers.IO) {
        calendarUseCases.getNotAvailableDaysInThisMonth.invoke(yearNumber, monthNumber, gpId).collect {
            when (it) {
                is Resource.Success -> {
                    _notAvailableDaysList.value = NotAvailableDaysInThisMonthState(notAvailableDaysList = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _notAvailableDaysList.value = NotAvailableDaysInThisMonthState(isLoading = true)
                }
                is Resource.Error -> {
                    _notAvailableDaysList.value = NotAvailableDaysInThisMonthState(error = it.message ?: "Unexpected error occured.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }

    fun getAvailableTimeSlotsOfParticularDay(yearNumber: Int, monthNumber: Int, monthDay: Int, gpId: String) = viewModelScope.launch(Dispatchers.IO) {
        calendarUseCases.getAvailableTimeSlotsOfParticularDay.invoke(yearNumber, monthNumber, monthDay, gpId).collect {
            when (it) {
                is Resource.Success -> {
                    _availableTimeSlots.value = AvailableTimeSlotsOfParticularDayState(availableTimeSlots = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _availableTimeSlots.value = AvailableTimeSlotsOfParticularDayState(isLoading = true)
                }
                is Resource.Error -> {
                    _availableTimeSlots.value = AvailableTimeSlotsOfParticularDayState(error = it.message ?: "Unexpected error occured.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }

    fun findFirstAvailableDay(monthNumber: Month, notAvailableDaysInThisMonth: List<LocalDate>): CalendarDay? {
        if(notAvailableDaysInThisMonth.contains(dateToday)) {
            val currentMonthLength = monthNumber.length(false)
            // +1 because we start from next day. already established in the if() that today is full.
            val currentDayOfTheMonth: Int = dateToday.dayOfMonth + 1
            for (currentLoopedDay in currentDayOfTheMonth..currentMonthLength)
            {
                if(!notAvailableDaysInThisMonth.contains(LocalDate.of(dateToday.year, dateToday.monthValue, currentLoopedDay))) {
                    return CalendarDay.from(dateToday.year, dateToday.monthValue, currentLoopedDay)
                }
            }
        } else {
            return CalendarDay.today()
        }
        return null
    }
}