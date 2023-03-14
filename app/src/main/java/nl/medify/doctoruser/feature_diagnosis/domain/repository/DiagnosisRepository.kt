package nl.medify.doctoruser.feature_diagnosis.domain.repository

import nl.medify.doctoruser.feature_diagnosis.domain.model.Diagnosis
import java.time.LocalDate

interface DiagnosisRepository {
    suspend fun getDiagnosisOfPatient(id: String): ArrayList<Diagnosis>

    suspend fun getDiagnosisOfMedicalRecord(mrId: String, cpId: String): ArrayList<Diagnosis>

    suspend fun createDiagnosis(
        title: String,
        diagnosisType: String,
        dateDiagnosed: LocalDate,
        cause: String,
        treatment: String,
        advice: String,
        medicalRecordID : String
    )
}