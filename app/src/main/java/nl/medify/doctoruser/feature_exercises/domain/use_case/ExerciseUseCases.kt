package nl.medify.doctoruser.feature_exercises.domain.use_case

import nl.medify.doctoruser.feature_exercises.domain.use_case.get_all_exercises.GetAllExercisesUseCase
import nl.medify.doctoruser.feature_exercises.domain.use_case.get_exercises_of_medical_record.GetExercisesOfMedicalRecordUseCase
import nl.medify.doctoruser.feature_exercises.domain.use_case.post_create_exercise.PostCreateExerciseUseCase

data class ExerciseUseCases(
    val getAllExercisesUseCase: GetAllExercisesUseCase,
    val getExercisesOfMedicalRecordUseCase: GetExercisesOfMedicalRecordUseCase,
    val postCreateExerciseUseCases: PostCreateExerciseUseCase
)