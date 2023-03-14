package nl.medify.doctoruser.feature_calendar.domain.model

import java.time.LocalDateTime

data class ShowAppointment(
    val appointmentID: String,
    val name: String,
    val dateAndTime: LocalDateTime,
    val address: String,
    val reasonForVisit: String,
)