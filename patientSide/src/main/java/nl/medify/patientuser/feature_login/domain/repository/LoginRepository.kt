package nl.medify.patientuser.feature_login.domain.repository

import nl.medify.utilities.data_classes.Patient

interface LoginRepository {

    suspend fun getPatientByEmail(email: String): Patient

}