package nl.medify.patientuser.feature_notes.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_notes.data.repository.NoteRepositoryImpl
import nl.medify.patientuser.feature_notes.domain.repository.NoteRepository
import nl.medify.patientuser.feature_notes.domain.use_case.NoteUseCases
import nl.medify.patientuser.feature_notes.domain.use_case.get_all_notes.GetAllNotesUseCase
import nl.medify.patientuser.feature_notes.domain.use_case.post_create_note.PostCreateNoteUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideNoteRepository(api: PatientAPIService): NoteRepository {
        return NoteRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getAllNotesUseCase = GetAllNotesUseCase(repository),
            postCreateNoteUseCases = PostCreateNoteUseCase(repository)
        )
    }
}