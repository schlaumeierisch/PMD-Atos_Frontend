package nl.medify.patientuser.feature_notes.domain.use_case.post_create_note

import nl.medify.patientuser.feature_notes.domain.repository.NoteRepository
import javax.inject.Inject

class PostCreateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(title: String, description: String, medicalRecordID: String) {
        repository.createNote(title, description, medicalRecordID)
    }
}