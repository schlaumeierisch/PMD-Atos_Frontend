package nl.medify.doctoruser.feature_login.data.repository

import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.doctoruser.feature_login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: MedifyAPIService
) : LoginRepository {

    override suspend fun getGeneralPractitionerByEmail(email: String): DoctorOrCareProvider {
        return api.getGeneralPractitionerByEmail(email)
    }

    override suspend fun getCareProviderByEmail(email: String): DoctorOrCareProvider {
        return api.getCareProviderByEmail(email)
    }

}