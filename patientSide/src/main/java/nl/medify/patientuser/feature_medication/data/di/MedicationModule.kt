package nl.medify.patientuser.feature_medication.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_medication.data.repository.MedicationRepositoryImpl
import nl.medify.patientuser.feature_medication.domain.repository.MedicationRepository
import nl.medify.patientuser.feature_medication.domain.use_case.MedicationUseCases
import nl.medify.patientuser.feature_medication.domain.use_case.get_all_medication.GetAllMedicationUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MedicationModule {

    @Provides
    @Singleton
    fun provideMedicationRepository(api: PatientAPIService): MedicationRepository {
        return MedicationRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDiagnosisUseCases(repository: MedicationRepository): MedicationUseCases {
        return MedicationUseCases(
            getAllMedicationUseCase = GetAllMedicationUseCase(repository)
        )
    }
}