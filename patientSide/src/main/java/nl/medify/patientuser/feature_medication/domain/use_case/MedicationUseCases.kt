package nl.medify.patientuser.feature_medication.domain.use_case

import nl.medify.patientuser.feature_medication.domain.use_case.get_all_medication.GetAllMedicationUseCase


data class MedicationUseCases (
    val getAllMedicationUseCase: GetAllMedicationUseCase,
)