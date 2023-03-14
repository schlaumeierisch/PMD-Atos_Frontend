package nl.medify.doctoruser.feature_exercises.domain.use_case.post_create_exercise

import nl.medify.doctoruser.feature_exercises.domain.repository.ExerciseRepository
import java.time.LocalDate
import javax.inject.Inject

class PostCreateExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {

    suspend operator fun invoke(title: String, description: String, startDate: LocalDate, endDate: LocalDate, medicalRecordID: String) {
        repository.createExercise(title, description, startDate, endDate, medicalRecordID)
    }
}