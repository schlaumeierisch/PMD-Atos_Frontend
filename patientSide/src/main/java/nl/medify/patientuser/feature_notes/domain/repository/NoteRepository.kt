package nl.medify.patientuser.feature_notes.domain.repository

import nl.medify.utilities.data_classes.Note

interface NoteRepository {
    suspend fun getNoteOfPatient(id: String): ArrayList<Note>

    suspend fun createNote(
        title: String,
        description: String,
        medicalRecordID : String
    )
}