package nl.medify.patientuser.feature_notes.domain.use_case

import nl.medify.patientuser.feature_notes.domain.use_case.get_all_notes.GetAllNotesUseCase
import nl.medify.patientuser.feature_notes.domain.use_case.post_create_note.PostCreateNoteUseCase


data class NoteUseCases(
    val getAllNotesUseCase: GetAllNotesUseCase,
    val postCreateNoteUseCases: PostCreateNoteUseCase
)