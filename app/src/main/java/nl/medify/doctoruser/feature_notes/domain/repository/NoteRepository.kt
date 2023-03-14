package nl.medify.doctoruser.feature_notes.domain.repository

import nl.medify.utilities.data_classes.Note

interface NoteRepository {

    suspend fun getNotesOfPatient(id: String): ArrayList<Note>

    suspend fun getNotesOfMedicalRecord(mrId: String, cpId: String): ArrayList<Note>

    suspend fun createNote(
        title: String,
        description: String,
        medicalRecordID : String
    )

}