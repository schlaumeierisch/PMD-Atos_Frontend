package nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.states

import java.time.LocalDate

data class NotAvailableDaysInThisMonthState(
    val isLoading: Boolean = false,
    val notAvailableDaysList: List<LocalDate> = emptyList(),
    val error: String = ""
)