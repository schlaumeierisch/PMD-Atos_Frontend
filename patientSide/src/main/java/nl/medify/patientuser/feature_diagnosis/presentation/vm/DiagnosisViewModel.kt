package nl.medify.patientuser.feature_diagnosis.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.patientuser.feature_diagnosis.domain.use_case.DiagnosisUseCases
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    private val diagnosisUseCases: DiagnosisUseCases
) : ViewModel() {

    private val _diagnosisList = MutableStateFlow(DiagnosisListState())
    var diagnoseList: StateFlow<DiagnosisListState> = _diagnosisList

    fun getAllDiagnoses(id: String) = viewModelScope.launch(Dispatchers.IO) {
        diagnosisUseCases.getAllDiagnosesUseCase.invoke(id).collect {
            when(it) {
                is Resource.Success -> {
                    _diagnosisList.value = DiagnosisListState(diagnosisList = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _diagnosisList.value = DiagnosisListState(isLoading = true)
                }
                is Resource.Error -> {
                    _diagnosisList.value = DiagnosisListState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }
}