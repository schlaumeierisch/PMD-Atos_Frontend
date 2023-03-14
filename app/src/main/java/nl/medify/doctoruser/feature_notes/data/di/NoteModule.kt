package nl.medify.doctoruser.feature_notes.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_notes.data.repository.NoteRepositoryImpl
import nl.medify.doctoruser.feature_notes.domain.repository.NoteRepository
import nl.medify.doctoruser.feature_notes.domain.use_case.NoteUseCases
import nl.medify.doctoruser.feature_notes.domain.use_case.get_all_notes.GetAllNotesUseCase
import nl.medify.doctoruser.feature_notes.domain.use_case.get_notes_of_medical_record.GetNotesOfMedicalRecordUseCase
import nl.medify.doctoruser.feature_notes.domain.use_case.post_create_note.PostCreateNoteUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideNoteRepository(api: MedifyAPIService): NoteRepository {
        return NoteRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getAllNotesUseCase = GetAllNotesUseCase(repository),
            getNotesOfMedicalRecordUseCase = GetNotesOfMedicalRecordUseCase(repository),
            postCreateNoteUseCase = PostCreateNoteUseCase(repository)
        )
    }
}