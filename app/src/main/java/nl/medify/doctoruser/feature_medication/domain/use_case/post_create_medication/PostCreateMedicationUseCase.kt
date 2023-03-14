package nl.medify.doctoruser.feature_medication.domain.use_case.post_create_medication

import nl.medify.doctoruser.feature_medication.domain.repository.MedicationRepository
import java.time.LocalDate
import javax.inject.Inject

class PostCreateMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(title: String, description: String, startDate: LocalDate, endDate: LocalDate, medicalRecordID: String) {
        repository.createMedication(title, description, startDate, endDate, medicalRecordID)
    }
}