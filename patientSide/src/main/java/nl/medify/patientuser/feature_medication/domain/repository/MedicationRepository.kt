package nl.medify.patientuser.feature_medication.domain.repository

import nl.medify.patientuser.feature_medication.domain.model.Medication

interface MedicationRepository {

    suspend fun getMedicationOfPatient(id: String): ArrayList<Medication>
}