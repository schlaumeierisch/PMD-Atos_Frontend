package nl.medify.doctoruser.feature_patient_list.domain.repository

import nl.medify.utilities.data_classes.Patient

interface PatientListRepository {

    suspend fun getPatientsOfGeneralPractitionerById(id: String): ArrayList<Patient>

    suspend fun getPatientsOfCareProviderById(id: String): ArrayList<Patient>

}