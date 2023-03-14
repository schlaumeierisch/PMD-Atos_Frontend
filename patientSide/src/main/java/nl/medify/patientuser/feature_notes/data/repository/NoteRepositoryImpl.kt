package nl.medify.patientuser.feature_notes.data.repository

import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_notes.domain.repository.NoteRepository
import nl.medify.utilities.data_classes.Note
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val api: PatientAPIService
) : NoteRepository {

    override suspend fun getNoteOfPatient(id: String): ArrayList<Note> {
        return api.getNotesOfPatient(id)
    }

    override suspend fun createNote(title: String, description: String, medicalRecordID : String) {
        api.createNote(title, description, medicalRecordID)
    }
}

