package nl.medify.patientuser.feature_login.domain.use_case

import nl.medify.patientuser.feature_login.domain.use_case.get_patient_by_email.GetPatientByEmailUseCase

data class LoginUseCases(
    val getPatientByEmailUseCase: GetPatientByEmailUseCase
)