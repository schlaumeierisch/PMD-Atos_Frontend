package nl.medify.doctoruser.feature_patient_list.data.repository

import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_patient_list.domain.repository.PatientListRepository
import nl.medify.utilities.data_classes.Patient
import javax.inject.Inject

class PatientListRepositoryImpl @Inject constructor(
    private val api: MedifyAPIService
) : PatientListRepository {

    override suspend fun getPatientsOfGeneralPractitionerById(id: String): ArrayList<Patient> {
        return api.getPatientsOfGeneralPractitioner(id)
    }

    override suspend fun getPatientsOfCareProviderById(id: String): ArrayList<Patient> {
        return api.getPatientsOfCareProviderById(id)
    }

}