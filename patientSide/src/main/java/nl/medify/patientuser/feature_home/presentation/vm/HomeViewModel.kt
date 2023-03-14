package nl.medify.patientuser.feature_home.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.patientuser.feature_medication.domain.use_case.MedicationUseCases
import nl.medify.patientuser.feature_medication.presentation.vm.MedicationListState
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val medicationUseCases: MedicationUseCases
) : ViewModel() {

    private val _medicationList = MutableStateFlow(MedicationListState())
    var medicationList: StateFlow<MedicationListState> = _medicationList

    fun getMedicationOfPatient(id: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.e("homeMade", "test")
        medicationUseCases.getAllMedicationUseCase.invoke(id).collect {
            when(it) {
                is Resource.Success -> {
                    _medicationList.value = MedicationListState(medicationList = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _medicationList.value = MedicationListState(isLoading = true)
                }
                is Resource.Error -> {
                    _medicationList.value = MedicationListState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name,"'it.data' is null")
                }
            }
        }
    }
}