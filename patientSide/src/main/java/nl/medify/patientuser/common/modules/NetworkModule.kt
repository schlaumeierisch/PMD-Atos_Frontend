package nl.medify.patientuser.common.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.common.api.RetrofitPatientApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMedifyAPI(): PatientAPIService {
        return RetrofitPatientApi.apiService
    }
}