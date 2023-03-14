package nl.medify.doctoruser.feature_patient_list.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.doctoruser.feature_patient_list.domain.use_case.PatientListUseCases
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class PatientListViewModel @Inject constructor(
    private val patientUseCases: PatientListUseCases
) : ViewModel() {

    private val _patientList = MutableStateFlow(PatientListState())
    var patientList: StateFlow<PatientListState> = _patientList

    var selectedPatient: Patient? = null

    fun getPatientsOfGeneralPractitionerById(id: String) = viewModelScope.launch(Dispatchers.IO) {
        patientUseCases.getPatientsOfGeneralPractitionerByIdUseCase.invoke(id).collect {
            when (it) {
                is Resource.Success -> {
                    _patientList.value = PatientListState(patientList = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _patientList.value = PatientListState(isLoading = true)
                }
                is Resource.Error -> {
                    _patientList.value = PatientListState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }

    fun getPatientsOfCareProviderById(id: String) = viewModelScope.launch(Dispatchers.IO) {
        patientUseCases.getPatientsOfCareProviderByIdUseCase.invoke(id).collect {
            when (it) {
                is Resource.Success -> {
                    _patientList.value = PatientListState(patientList = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _patientList.value = PatientListState(isLoading = true)
                }
                is Resource.Error -> {
                    _patientList.value = PatientListState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }
}