package nl.medify.doctoruser.feature_calendar.domain.model

data class CreateAppointment(
    val time: String,
    val reason: String,
    val patientID: String,
    val gpID: String?,
    val cpID: String?
)
