package nl.medify.patientuser.feature_exercises.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_exercises.data.repository.ExerciseRepositoryImpl
import nl.medify.patientuser.feature_exercises.domain.repository.ExerciseRepository
import nl.medify.patientuser.feature_exercises.domain.use_case.ExerciseUseCases
import nl.medify.patientuser.feature_exercises.domain.use_case.get_all_exercises.GetAllExercisesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExerciseModule {

    @Provides
    @Singleton
    fun provideExerciseRepository(api: PatientAPIService): ExerciseRepository {
        return ExerciseRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideExerciseUseCases(repository: ExerciseRepository): ExerciseUseCases {
        return ExerciseUseCases(
            getAllExercisesUseCase = GetAllExercisesUseCase(repository),
        )
    }
}