package nl.medify.patientuser.feature_exercises.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.patientuser.feature_exercises.domain.use_case.ExerciseUseCases
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases
) : ViewModel() {

    private val _exerciseList = MutableStateFlow(ExerciseListState())
    var exerciseList: StateFlow<ExerciseListState> = _exerciseList

    fun getAllExercises(id: String) = viewModelScope.launch(Dispatchers.IO) {
        exerciseUseCases.getAllExercisesUseCase.invoke(id).collect {
            when(it) {
                is Resource.Success -> {
                    _exerciseList.value = ExerciseListState(exerciseList = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _exerciseList.value = ExerciseListState(isLoading = true)
                }
                is Resource.Error -> {
                    _exerciseList.value = ExerciseListState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }
}