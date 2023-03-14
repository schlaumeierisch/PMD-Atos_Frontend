package nl.medify.doctoruser.feature_medication.data.repository

import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_medication.domain.model.Medication
import nl.medify.doctoruser.feature_medication.domain.repository.MedicationRepository
import java.time.LocalDate

import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(
    private val api: MedifyAPIService
) : MedicationRepository {

    override suspend fun getMedicationOfPatient(id: String): ArrayList<Medication> {
        return api.getAllMedication(id)
    }

    override suspend fun getMedicationOfMedicalRecord(
        mrId: String,
        cpId: String
    ): ArrayList<Medication> {
        return api.getMedicationOfMedicalRecord(mrId, cpId)
    }

    override suspend fun createMedication(
        title: String,
        description: String,
        startDate: LocalDate,
        endDate: LocalDate,
        medicalRecordID: String
    ) {
        api.createMedication(title, description, startDate, endDate, medicalRecordID)
    }

}