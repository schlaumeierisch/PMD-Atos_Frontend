package nl.medify.patientuser.feature_calendar.presentation.calendar_screen.vm

import nl.medify.patientuser.feature_calendar.domain.model.ShowAppointment

data class AppointmentListState(
    val isLoading: Boolean = false,
    val appointmentList: List<ShowAppointment> = emptyList(),
    val error: String = ""
)