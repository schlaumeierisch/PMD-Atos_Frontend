package nl.medify.patientuser.feature_diagnosis.data.repository

import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.patientuser.feature_diagnosis.domain.repository.DiagnosisRepository
import javax.inject.Inject

class DiagnosisRepositoryImpl @Inject constructor(
    private val api: PatientAPIService
) : DiagnosisRepository {

    override suspend fun getDiagnosisOfPatient(id: String): ArrayList<Diagnosis> {
        return api.getAllDiagnoses(id)
    }
}