package nl.medify.doctoruser.feature_notes.domain.use_case.get_notes_of_medical_record

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_notes.domain.repository.NoteRepository
import nl.medify.utilities.data_classes.Note
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetNotesOfMedicalRecordUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(mrId: String, cpId: String): Flow<Resource<ArrayList<Note>>> = flow {
        try {
            emit(Resource.Loading())
            val notes = repository.getNotesOfMedicalRecord(mrId, cpId)
            emit(Resource.Success(notes))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}