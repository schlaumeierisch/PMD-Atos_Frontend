package nl.medify.doctoruser.feature_login.domain.repository

import nl.medify.doctoruser.feature_calendar.domain.model.DoctorOrCareProvider

interface LoginRepository {

    suspend fun getGeneralPractitionerByEmail(email: String): DoctorOrCareProvider

    suspend fun getCareProviderByEmail(email: String): DoctorOrCareProvider

}