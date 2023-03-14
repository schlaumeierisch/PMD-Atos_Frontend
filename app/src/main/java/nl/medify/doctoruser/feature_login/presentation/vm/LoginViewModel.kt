package nl.medify.doctoruser.feature_login.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.doctoruser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.doctoruser.feature_login.domain.model.UserRole
import nl.medify.doctoruser.feature_login.domain.use_case.LoginUseCases
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private val _generalPractitioner = MutableStateFlow(LoginState())
    var generalPractitioner: StateFlow<LoginState> = _generalPractitioner

    private val _careProvider = MutableStateFlow(LoginState())
    var careProvider: StateFlow<LoginState> = _careProvider

    var loggedInUser: DoctorOrCareProvider? = null
    var loggedInUserRole: UserRole? = null

    fun getGeneralPractitionerByEmail(email: String) = viewModelScope.launch(Dispatchers.IO) {
        loginUseCases.getGeneralPractitionerByEmailUseCase.invoke(email).collect {
            when (it) {
                is Resource.Success -> {
                    _generalPractitioner.value = LoginState(loggedInUser = it.data)
                    loggedInUser = it.data
                    loggedInUserRole = UserRole.GENERAL_PRACTITIONER
                }
                is Resource.Loading -> {
                    _generalPractitioner.value = LoginState(isLoading = true)
                }
                is Resource.Error -> {
                    _generalPractitioner.value = LoginState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }

    fun getCareProviderByEmail(email: String) = viewModelScope.launch(Dispatchers.IO) {
        loginUseCases.getCareProviderByEmailUseCase.invoke(email).collect {
            when (it) {
                is Resource.Success -> {
                    _careProvider.value = LoginState(loggedInUser = it.data)
                    loggedInUser = it.data
                    loggedInUserRole = UserRole.CARE_PROVIDER
                }
                is Resource.Loading -> {
                    _careProvider.value = LoginState(isLoading = true)
                }
                is Resource.Error -> {
                    _careProvider.value = LoginState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }
}