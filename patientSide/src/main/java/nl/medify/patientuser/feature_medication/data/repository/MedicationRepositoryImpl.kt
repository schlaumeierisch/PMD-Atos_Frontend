package nl.medify.patientuser.feature_medication.data.repository

import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_medication.domain.model.Medication
import nl.medify.patientuser.feature_medication.domain.repository.MedicationRepository

import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(
    private val api: PatientAPIService
) : MedicationRepository {


    override suspend fun getMedicationOfPatient(id: String): ArrayList<Medication> {
        return api.getAllMedication(id)
    }
}