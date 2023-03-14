package nl.medify.patientuser.feature_permission_linking.feature_care_providers.data.repository

import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.patientuser.feature_permission_linking.feature_care_providers.domain.repository.CareProviderRepository
import javax.inject.Inject

class CareProviderRepositoryImpl @Inject constructor(
    private val api: PatientAPIService
) : CareProviderRepository {
    override suspend fun getCareProviderOfPatient(id: String): ArrayList<DoctorOrCareProvider> {
        return api.getAllCareProvidersOfPatientByID(id)
    }
}