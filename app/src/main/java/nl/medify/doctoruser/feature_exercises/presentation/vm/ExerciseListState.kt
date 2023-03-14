package nl.medify.doctoruser.feature_exercises.presentation.vm

import nl.medify.doctoruser.feature_exercises.domain.model.Exercise

data class ExerciseListState(
    val isLoading: Boolean = false,
    val exerciseList: List<Exercise> = emptyList(),
    val error: String = ""
)