package nl.medify.patientuser.feature_permission_linking.feature_care_providers.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_permission_linking.feature_care_providers.data.repository.CareProviderRepositoryImpl
import nl.medify.patientuser.feature_permission_linking.feature_care_providers.domain.repository.CareProviderRepository
import nl.medify.patientuser.feature_permission_linking.feature_care_providers.domain.use_case.CareProviderUseCases
import nl.medify.patientuser.feature_permission_linking.feature_care_providers.domain.use_case.get_all_care_providers.GetAllCareProvidersUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CareProviderModule {

    @Provides
    @Singleton
    fun provideCareProviderRepository(api: PatientAPIService): CareProviderRepository {
        return CareProviderRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCareProviderUseCases(repository: CareProviderRepository): CareProviderUseCases {
        return CareProviderUseCases(
            getAllCareProvidersUseCase = GetAllCareProvidersUseCase(repository)
        )

    }

}