package nl.medify.doctoruser.feature_login.presentation.vm

import nl.medify.doctoruser.feature_calendar.domain.model.DoctorOrCareProvider

data class LoginState(
    val isLoading: Boolean = false,
    val loggedInUser: DoctorOrCareProvider? = null,
    val error: String = ""
)