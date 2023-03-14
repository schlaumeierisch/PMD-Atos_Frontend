package nl.medify.patientuser.feature_login.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.patientuser.feature_login.domain.use_case.LoginUseCases
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private val _patient = MutableStateFlow(LoginState())
    var patient: StateFlow<LoginState> = _patient

    var loggedInPatient: Patient? = null

    fun getPatientByEmail(email: String) = viewModelScope.launch(Dispatchers.IO) {
        loginUseCases.getPatientByEmailUseCase.invoke(email).collect {
            when (it) {
                is Resource.Success -> {
                    _patient.value = LoginState(loggedInPatient = it.data)
                    loggedInPatient = it.data
                }
                is Resource.Loading -> {
                    _patient.value = LoginState(isLoading = true)
                }
                is Resource.Error -> {
                    _patient.value = LoginState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }
}