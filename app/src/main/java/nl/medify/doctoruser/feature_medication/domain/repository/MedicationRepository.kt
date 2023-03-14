package nl.medify.doctoruser.feature_medication.domain.repository

import nl.medify.doctoruser.feature_medication.domain.model.Medication
import java.time.LocalDate

interface MedicationRepository {

    suspend fun getMedicationOfPatient(id: String): ArrayList<Medication>

    suspend fun getMedicationOfMedicalRecord(mrId: String, cpId: String): ArrayList<Medication>

    suspend fun createMedication(
        title: String,
        description: String,
        startDate: LocalDate,
        endDate: LocalDate,
        medicalRecordID : String
    )

}