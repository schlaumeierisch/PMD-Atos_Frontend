package nl.medify.doctoruser.feature_calendar.presentation.calendar_screen.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.doctoruser.feature_calendar.domain.model.ShowAppointment
import nl.medify.doctoruser.feature_calendar.domain.use_case.CalendarUseCases
import nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.states.GeoCodesState
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    var selectedAppointment: ShowAppointment? = null
        private set

    fun saveSelectedAppointment(showSelectedAppointment: ShowAppointment) {
        selectedAppointment = showSelectedAppointment
    }

    private val _geoCodes = MutableStateFlow(GeoCodesState())
    var geoCodes: StateFlow<GeoCodesState> = _geoCodes

    private val _appointmentList = MutableStateFlow(AppointmentListState())
    var appointmentList: StateFlow<AppointmentListState> = _appointmentList

    fun getAllAppointmentsOfDoctor(id: String) = viewModelScope.launch(Dispatchers.IO) {
        calendarUseCases.getAllAppointmentsOfDoctorUseCase.invoke(id).collect {
            when(it) {
                is Resource.Success -> {
                    _appointmentList.value = AppointmentListState(appointmentList = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _appointmentList.value = AppointmentListState(isLoading = true)
                }
                is Resource.Error -> {
                    _appointmentList.value = AppointmentListState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }

    fun cancelAppointment(appointmentID: String) = viewModelScope.launch(Dispatchers.IO) {
        calendarUseCases.cancelAppointmentUseCase.invoke(appointmentID)
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
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }
}

