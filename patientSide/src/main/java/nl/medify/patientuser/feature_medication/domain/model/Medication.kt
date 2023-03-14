package nl.medify.patientuser.feature_medication.domain.model

class Medication(
    val id: String,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val medicalRecordId: String
)
