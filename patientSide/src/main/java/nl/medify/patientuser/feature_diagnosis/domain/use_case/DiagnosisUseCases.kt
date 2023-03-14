package nl.medify.patientuser.feature_diagnosis.domain.use_case

import nl.medify.patientuser.feature_diagnosis.domain.use_case.get_all_diagnoses.GetAllDiagnosesUseCase

data class DiagnosisUseCases (
    val getAllDiagnosesUseCase: GetAllDiagnosesUseCase,
)