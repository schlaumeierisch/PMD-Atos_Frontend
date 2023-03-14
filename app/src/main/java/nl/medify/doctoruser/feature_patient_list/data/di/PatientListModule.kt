package nl.medify.doctoruser.feature_patient_list.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_patient_list.data.repository.PatientListRepositoryImpl
import nl.medify.doctoruser.feature_patient_list.domain.repository.PatientListRepository
import nl.medify.doctoruser.feature_patient_list.domain.use_case.PatientListUseCases
import nl.medify.doctoruser.feature_patient_list.domain.use_case.get_patients_of_care_provider_by_id.GetPatientsOfCareProviderByIdUseCase
import nl.medify.doctoruser.feature_patient_list.domain.use_case.get_patients_of_general_practitioner_by_id.GetPatientsOfGeneralPractitionerByIdUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PatientListModule {

    @Provides
    @Singleton
    fun providePatientListRepository(api: MedifyAPIService): PatientListRepository {
        return PatientListRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePatientListUseCases(repository: PatientListRepository): PatientListUseCases {
        return PatientListUseCases(
            getPatientsOfGeneralPractitionerByIdUseCase = GetPatientsOfGeneralPractitionerByIdUseCase(repository),
            getPatientsOfCareProviderByIdUseCase = GetPatientsOfCareProviderByIdUseCase(repository)
        )
    }
}