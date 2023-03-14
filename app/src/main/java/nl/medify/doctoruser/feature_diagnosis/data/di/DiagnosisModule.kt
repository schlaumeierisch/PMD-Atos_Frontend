package nl.medify.doctoruser.feature_diagnosis.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_diagnosis.data.repository.DiagnosisRepositoryImpl
import nl.medify.doctoruser.feature_diagnosis.domain.repository.DiagnosisRepository
import nl.medify.doctoruser.feature_diagnosis.domain.use_case.DiagnosisUseCases
import nl.medify.doctoruser.feature_diagnosis.domain.use_case.get_all_diagnoses.GetAllDiagnosesUseCase
import nl.medify.doctoruser.feature_diagnosis.domain.use_case.get_diagnosis_of_medical_record.GetDiagnosisOfMedicalRecordUseCase
import nl.medify.doctoruser.feature_diagnosis.domain.use_case.post_create_diagnosis.PostCreateDiagnosisUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiagnosisModule {

    @Provides
    @Singleton
    fun provideDiagnosisRepository(api: MedifyAPIService): DiagnosisRepository {
        return DiagnosisRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDiagnosisUseCases(repository: DiagnosisRepository): DiagnosisUseCases {
        return DiagnosisUseCases(
            getAllDiagnosesUseCase = GetAllDiagnosesUseCase(repository),
            getDiagnosisOfMedicalRecordUseCase = GetDiagnosisOfMedicalRecordUseCase(repository),
            postCreateDiagnosisUseCases = PostCreateDiagnosisUseCase(repository)
        )
    }
}