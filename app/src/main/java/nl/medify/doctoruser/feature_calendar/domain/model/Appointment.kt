package nl.medify.doctoruser.feature_calendar.domain.model

data class Appointment(
    val id: String,
    val dateTime: String,
    val reason: String,
    val patientId: String,
    val cpId: String? = null,
    val gpId: String? = null,
)

