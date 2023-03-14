package nl.medify.doctoruser.feature_medication.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_medication.data.repository.MedicationRepositoryImpl
import nl.medify.doctoruser.feature_medication.domain.repository.MedicationRepository
import nl.medify.doctoruser.feature_medication.domain.use_case.MedicationUseCases
import nl.medify.doctoruser.feature_medication.domain.use_case.get_all_medication.GetAllMedicationUseCase
import nl.medify.doctoruser.feature_medication.domain.use_case.get_medication_of_medical_record.GetMedicationOfMedicalRecordUseCase
import nl.medify.doctoruser.feature_medication.domain.use_case.post_create_medication.PostCreateMedicationUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MedicationModule {

    @Provides
    @Singleton
    fun provideMedicationRepository(api: MedifyAPIService): MedicationRepository {
        return MedicationRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDiagnosisUseCases(repository: MedicationRepository): MedicationUseCases {
        return MedicationUseCases(
            getAllMedicationUseCase = GetAllMedicationUseCase(repository),
            getMedicationOfMedicalRecordUseCase = GetMedicationOfMedicalRecordUseCase(repository),
            postCreateMedicationUseCase = PostCreateMedicationUseCase(repository)
        )
    }
}