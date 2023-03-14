package nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.data.repository.PermissionsRepositoryImpl
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.repository.PermissionsRepository
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.PermissionsUseCases
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.post_diagnosis_permission_link.PostDiagnosisPermissionLinkUseCase
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.post_exercise_permission_link.PostExercisePermissionLinkUseCase
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.post_medication_permission_link.PostMedicationPermissionLinkUseCase
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.post_note_permission_link.PostNotePermissionLinkUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PermissionOverviewModule {

    @Provides
    @Singleton
    fun providePermissionsRepository(api: PatientAPIService): PermissionsRepository {
        return PermissionsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePermissionsUseCases(repository: PermissionsRepository): PermissionsUseCases {
        return PermissionsUseCases(
            postDiagnosisPermissionLinkUseCase = PostDiagnosisPermissionLinkUseCase(repository),
            postExercisePermissionLinkUseCase = PostExercisePermissionLinkUseCase(repository),
            postMedicationPermissionLinkUseCase = PostMedicationPermissionLinkUseCase(repository),
            postNotePermissionLinkUseCase = PostNotePermissionLinkUseCase(repository)
        )
    }
}