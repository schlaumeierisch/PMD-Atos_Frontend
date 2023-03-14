package nl.medify.doctoruser.feature_diagnosis.domain.use_case

import nl.medify.doctoruser.feature_diagnosis.domain.use_case.get_all_diagnoses.GetAllDiagnosesUseCase
import nl.medify.doctoruser.feature_diagnosis.domain.use_case.get_diagnosis_of_medical_record.GetDiagnosisOfMedicalRecordUseCase
import nl.medify.doctoruser.feature_diagnosis.domain.use_case.post_create_diagnosis.PostCreateDiagnosisUseCase

data class DiagnosisUseCases (
    val getAllDiagnosesUseCase: GetAllDiagnosesUseCase,
    val getDiagnosisOfMedicalRecordUseCase: GetDiagnosisOfMedicalRecordUseCase,
    val postCreateDiagnosisUseCases: PostCreateDiagnosisUseCase
)