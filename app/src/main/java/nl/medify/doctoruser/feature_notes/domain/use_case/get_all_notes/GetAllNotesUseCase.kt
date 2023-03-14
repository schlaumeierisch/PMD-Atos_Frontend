package nl.medify.doctoruser.feature_notes.domain.use_case.get_all_notes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_notes.domain.repository.NoteRepository
import nl.medify.utilities.data_classes.Note
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(id: String): Flow<Resource<ArrayList<Note>>> = flow {
        try {
            emit(Resource.Loading())
            val notes = repository.getNotesOfPatient(id)
            emit(Resource.Success(notes))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}