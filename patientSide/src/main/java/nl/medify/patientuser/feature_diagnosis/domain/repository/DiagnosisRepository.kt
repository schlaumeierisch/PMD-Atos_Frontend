package nl.medify.patientuser.feature_diagnosis.domain.repository

import nl.medify.patientuser.feature_diagnosis.domain.model.Diagnosis

interface DiagnosisRepository {
    suspend fun getDiagnosisOfPatient(id: String): ArrayList<Diagnosis>
}