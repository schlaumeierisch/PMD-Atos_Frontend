package nl.medify.patientuser.feature_calendar.domain.use_case.post_create_appointment

import nl.medify.patientuser.feature_calendar.domain.repository.CalendarRepository
import java.time.LocalDateTime
import javax.inject.Inject

class PostCreateAppointmentUseCase @Inject constructor(
    private val repository: CalendarRepository
) {

    suspend operator fun invoke(dateTime: LocalDateTime, reason: String, patientId: String, gpId: String?, cpId: String?) {
        repository.createAppointment(dateTime, reason, patientId, gpId, cpId)
    }
}