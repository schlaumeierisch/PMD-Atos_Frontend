package nl.medify.doctoruser.feature_notes.domain.use_case

import nl.medify.doctoruser.feature_notes.domain.use_case.get_all_notes.GetAllNotesUseCase
import nl.medify.doctoruser.feature_notes.domain.use_case.get_notes_of_medical_record.GetNotesOfMedicalRecordUseCase
import nl.medify.doctoruser.feature_notes.domain.use_case.post_create_note.PostCreateNoteUseCase

data class NoteUseCases (
    val getAllNotesUseCase: GetAllNotesUseCase,
    val getNotesOfMedicalRecordUseCase: GetNotesOfMedicalRecordUseCase,
    val postCreateNoteUseCase: PostCreateNoteUseCase
)