package nl.medify.doctoruser.feature_exercises.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_exercises.data.repository.ExerciseRepositoryImpl
import nl.medify.doctoruser.feature_exercises.domain.repository.ExerciseRepository
import nl.medify.doctoruser.feature_exercises.domain.use_case.ExerciseUseCases
import nl.medify.doctoruser.feature_exercises.domain.use_case.get_all_exercises.GetAllExercisesUseCase
import nl.medify.doctoruser.feature_exercises.domain.use_case.get_exercises_of_medical_record.GetExercisesOfMedicalRecordUseCase
import nl.medify.doctoruser.feature_exercises.domain.use_case.post_create_exercise.PostCreateExerciseUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExerciseModule {

    @Provides
    @Singleton
    fun provideExerciseRepository(api: MedifyAPIService): ExerciseRepository {
        return ExerciseRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideExerciseUseCases(repository: ExerciseRepository): ExerciseUseCases {
        return ExerciseUseCases(
            getAllExercisesUseCase = GetAllExercisesUseCase(repository),
            getExercisesOfMedicalRecordUseCase = GetExercisesOfMedicalRecordUseCase(repository),
            postCreateExerciseUseCases = PostCreateExerciseUseCase(repository)
        )
    }
}