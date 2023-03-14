package nl.medify.patientuser.feature_diagnosis.domain.model

data class Diagnosis (
    val id: String,
    val title: String,
    val diagnosisType: String,
    val dateDiagnosed: String,
    val cause: String,
    val treatment: String,
    val advice: String,
    val medicalRecordId: String)