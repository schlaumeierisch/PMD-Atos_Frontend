package nl.medify.doctoruser.feature_login.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_login.data.repository.LoginRepositoryImpl
import nl.medify.doctoruser.feature_login.domain.repository.LoginRepository
import nl.medify.doctoruser.feature_login.domain.use_case.LoginUseCases
import nl.medify.doctoruser.feature_login.domain.use_case.get_care_provider_by_email.GetCareProviderByEmailUseCase
import nl.medify.doctoruser.feature_login.domain.use_case.get_general_practitioner_by_email.GetGeneralPractitionerByEmailUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginRepository(api: MedifyAPIService): LoginRepository {
        return LoginRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideLoginUseCases(repository: LoginRepository): LoginUseCases {
        return LoginUseCases(
            getGeneralPractitionerByEmailUseCase = GetGeneralPractitionerByEmailUseCase(repository),
            getCareProviderByEmailUseCase = GetCareProviderByEmailUseCase(repository)
        )
    }
}