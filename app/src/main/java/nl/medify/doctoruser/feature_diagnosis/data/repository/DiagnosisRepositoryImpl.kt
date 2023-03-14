package nl.medify.doctoruser.feature_diagnosis.data.repository

import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.doctoruser.feature_diagnosis.domain.repository.DiagnosisRepository
import java.time.LocalDate
import javax.inject.Inject

class DiagnosisRepositoryImpl @Inject constructor(
    private val api: MedifyAPIService
) : DiagnosisRepository {

    override suspend fun getDiagnosisOfPatient(id: String): ArrayList<Diagnosis> {
        return api.getAllDiagnoses(id)
    }

    override suspend fun getDiagnosisOfMedicalRecord(
        mrId: String,
        cpId: String
    ): ArrayList<Diagnosis> {
        return api.getDiagnosisOfMedicalRecord(mrId, cpId)
    }

    override suspend fun createDiagnosis(
        title: String,
        diagnosisType: String,
        dateDiagnosed: LocalDate,
        cause: String,
        treatment: String,
        advice: String,
        medicalRecordID: String
    ) {
        api.createDiagnosis(
            title,
            diagnosisType,
            dateDiagnosed,
            cause,
            treatment,
            advice,
            medicalRecordID
        )
    }
}