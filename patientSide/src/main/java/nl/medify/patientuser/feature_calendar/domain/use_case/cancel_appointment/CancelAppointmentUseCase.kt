package nl.medify.patientuser.feature_calendar.domain.use_case.cancel_appointment

import nl.medify.patientuser.feature_calendar.domain.repository.CalendarRepository
import javax.inject.Inject

class CancelAppointmentUseCase @Inject constructor(
    private val repository: CalendarRepository
) {

    suspend operator fun invoke(appointmentID: String) {
        repository.cancelAppointment(appointmentID)
    }
}