package nl.medify.doctoruser.feature_profile.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_profile.data.repository.ImageRepositoryImpl
import nl.medify.doctoruser.feature_profile.domain.repository.ImageRepository
import nl.medify.doctoruser.feature_profile.domain.use_case.ImageUseCases
import nl.medify.doctoruser.feature_profile.domain.use_case.get_image_by_patient_id.GetImageByPatientIdUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageModule {

    @Provides
    @Singleton
    fun provideImageRepository(api: MedifyAPIService): ImageRepository {
        return ImageRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideImageUseCases(repository: ImageRepository): ImageUseCases {
        return ImageUseCases(
            getImageByPatientIdUseCase = GetImageByPatientIdUseCase(repository),
        )
    }
}