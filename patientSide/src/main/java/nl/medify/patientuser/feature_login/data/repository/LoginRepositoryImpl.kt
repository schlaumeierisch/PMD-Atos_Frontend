package nl.medify.patientuser.feature_login.data.repository

import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_login.domain.repository.LoginRepository
import nl.medify.utilities.data_classes.Patient
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: PatientAPIService
) : LoginRepository {

    override suspend fun getPatientByEmail(email: String): Patient {
        return api.getPatientByEmail(email)
    }

}