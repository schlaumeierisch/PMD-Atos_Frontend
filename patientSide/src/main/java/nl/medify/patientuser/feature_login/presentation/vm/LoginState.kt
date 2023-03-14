package nl.medify.patientuser.feature_login.presentation.vm

import nl.medify.utilities.data_classes.Patient
data class LoginState(
    val isLoading: Boolean = false,
    val loggedInPatient: Patient? = null,
    val error: String = ""
)