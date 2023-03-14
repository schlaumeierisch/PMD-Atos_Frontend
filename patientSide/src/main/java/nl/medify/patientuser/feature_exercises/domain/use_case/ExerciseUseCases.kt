package nl.medify.patientuser.feature_exercises.domain.use_case

import nl.medify.patientuser.feature_exercises.domain.use_case.get_all_exercises.GetAllExercisesUseCase


data class ExerciseUseCases(
    val getAllExercisesUseCase: GetAllExercisesUseCase,
)