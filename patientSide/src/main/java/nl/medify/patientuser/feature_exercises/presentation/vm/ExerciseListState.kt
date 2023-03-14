package nl.medify.patientuser.feature_exercises.presentation.vm

import nl.medify.patientuser.feature_exercises.domain.model.Exercise

data class ExerciseListState(
    val isLoading: Boolean = false,
    val exerciseList: List<Exercise> = emptyList(),
    val error: String = ""
)