package nl.medify.doctoruser.feature_diagnosis.domain.use_case.post_create_diagnosis

import nl.medify.doctoruser.feature_diagnosis.domain.repository.DiagnosisRepository
import java.time.LocalDate
import javax.inject.Inject

class PostCreateDiagnosisUseCase @Inject constructor(
    private val repository: DiagnosisRepository
) {

    suspend operator fun invoke(title: String, diagnosisType: String, dateDiagnosed: LocalDate, cause: String, treatment: String, advice: String, medicalRecordID: String) {
        repository.createDiagnosis(title, diagnosisType, dateDiagnosed, cause, treatment, advice, medicalRecordID)
    }
}