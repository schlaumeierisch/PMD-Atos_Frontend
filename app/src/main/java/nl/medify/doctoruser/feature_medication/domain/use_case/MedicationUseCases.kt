package nl.medify.doctoruser.feature_medication.domain.use_case

import nl.medify.doctoruser.feature_medication.domain.use_case.get_all_medication.GetAllMedicationUseCase
import nl.medify.doctoruser.feature_medication.domain.use_case.get_medication_of_medical_record.GetMedicationOfMedicalRecordUseCase
import nl.medify.doctoruser.feature_medication.domain.use_case.post_create_medication.PostCreateMedicationUseCase


data class MedicationUseCases (
    val getAllMedicationUseCase: GetAllMedicationUseCase,
    val getMedicationOfMedicalRecordUseCase: GetMedicationOfMedicalRecordUseCase,
    val postCreateMedicationUseCase: PostCreateMedicationUseCase
)