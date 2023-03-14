package nl.medify.patientuser.feature_exercises.domain.model

class Exercise(
    val id: String,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val medicalRecordId: String
)
