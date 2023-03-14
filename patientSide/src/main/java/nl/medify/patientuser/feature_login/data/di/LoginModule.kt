package nl.medify.patientuser.feature_login.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_login.data.repository.LoginRepositoryImpl
import nl.medify.patientuser.feature_login.domain.repository.LoginRepository
import nl.medify.patientuser.feature_login.domain.use_case.LoginUseCases
import nl.medify.patientuser.feature_login.domain.use_case.get_patient_by_email.GetPatientByEmailUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginRepository(api: PatientAPIService): LoginRepository {
        return LoginRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideLoginUseCases(repository: LoginRepository): LoginUseCases {
        return LoginUseCases(
            getPatientByEmailUseCase = GetPatientByEmailUseCase(repository),
        )
    }
}