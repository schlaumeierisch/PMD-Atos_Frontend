package nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.states

import nl.medify.utilities.data_classes.TimeSlot

data class AvailableTimeSlotsOfParticularDayState(
    val isLoading: Boolean = false,
    val availableTimeSlots: List<TimeSlot> = emptyList(),
    val error: String = ""
)