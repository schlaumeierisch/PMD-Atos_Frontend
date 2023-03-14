package nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.doctoruser.feature_calendar.domain.use_case.CalendarUseCases
import nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.states.AllPatientsState
import nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.states.GeoCodesState
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.util.Resource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    var chosenDateTime: LocalDateTime? = null; private set

    var chosenTime: LocalTime? = null; private set
    var reasonOfVisit: String? = null; private set
    var selectedPatient: Patient? = null; private set
    var chosenDate: LocalDate? = null; private set

    fun saveDate(date: LocalDate) { chosenDate = date }
    fun saveTime(time: LocalTime) { chosenTime = time; chosenDateTime = chosenTime?.atDate(chosenDate) }
    fun saveReasonOfVisit(reason: String) { reasonOfVisit = reason }
    fun saveSelectedPatient(patient: Patient) { selectedPatient = patient }

    private val _allPatients = MutableStateFlow(AllPatientsState())
    var allPatients: StateFlow<AllPatientsState> = _allPatients

    private val _geoCodes = MutableStateFlow(GeoCodesState())
    var geoCodes: StateFlow<GeoCodesState> = _geoCodes

    fun getAllPatientsOfDoctor(id: String) = viewModelScope.launch(Dispatchers.IO) {
        calendarUseCases.getAllPatientsOfDoctorUseCase.invoke(id).collect {
            when (it) {
                is Resource.Success -> {
                    _allPatients.value = AllPatientsState(allPatientsOfDoctorOrCareProvider = it.data)
                }
                is Resource.Loading -> {
                    _allPatients.value = AllPatientsState(isLoading = true)
                }
                is Resource.Error -> {
                    _allPatients.value = AllPatientsState(error = it.message ?: "Unexpected error occurred.")
                }
                is Resource.Empty -> {
                    _allPatients.value = AllPatientsState(allPatientsOfDoctorOrCareProvider = ArrayList())
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }

    fun postCreateAppointment() = viewModelScope.launch(Dispatchers.IO) {
        // TODO: Dynamically get user of doctorSide's gpID/cpID
        calendarUseCases.postCreateAppointmentUseCase.invoke(chosenDateTime!!, reasonOfVisit!!, selectedPatient!!.id, "dom-id-gp-001", null)
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
                    _geoCodes.value = GeoCodesState(error = it.message ?: "Unexpected error occurred.")
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
}