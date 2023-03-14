package nl.medify.patientuser.feature_diagnosis.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_diagnosis.data.repository.DiagnosisRepositoryImpl
import nl.medify.patientuser.feature_diagnosis.domain.repository.DiagnosisRepository
import nl.medify.patientuser.feature_diagnosis.domain.use_case.DiagnosisUseCases
import nl.medify.patientuser.feature_diagnosis.domain.use_case.get_all_diagnoses.GetAllDiagnosesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiagnosisModule {

    @Provides
    @Singleton
    fun provideDiagnosisRepository(api: PatientAPIService): DiagnosisRepository {
        return DiagnosisRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDiagnosisUseCases(repository: DiagnosisRepository): DiagnosisUseCases {
        return DiagnosisUseCases(
            getAllDiagnosesUseCase = GetAllDiagnosesUseCase(repository)
        )
    }
}