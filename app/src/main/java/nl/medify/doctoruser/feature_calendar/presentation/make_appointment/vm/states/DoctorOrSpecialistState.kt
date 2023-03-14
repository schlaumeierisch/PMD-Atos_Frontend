package nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.states

import nl.medify.doctoruser.feature_calendar.domain.model.DoctorOrCareProvider

data class DoctorOrSpecialistState(
    val isLoading: Boolean = false,
    val doctorOrSpecialist: DoctorOrCareProvider? = null,
    val error: String = ""
)