package nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.states

import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider

data class DoctorOrSpecialistState(
    val isLoading: Boolean = false,
    val doctorOrSpecialist: DoctorOrCareProvider? = null,
    val error: String = ""
)