package nl.medify.doctoruser.feature_notes.data.repository

import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_notes.domain.repository.NoteRepository
import nl.medify.utilities.data_classes.Note
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val api: MedifyAPIService
) : NoteRepository {

    override suspend fun getNotesOfPatient(id: String): ArrayList<Note> {
        return api.getNotesOfPatient(id)
    }

    override suspend fun getNotesOfMedicalRecord(mrId: String, cpId: String): ArrayList<Note> {
        return api.getNotesOfMedicalRecord(mrId, cpId)
    }

    override suspend fun createNote(title: String, description: String, medicalRecordID: String) {
        api.createNote(title, description, medicalRecordID)
    }

}