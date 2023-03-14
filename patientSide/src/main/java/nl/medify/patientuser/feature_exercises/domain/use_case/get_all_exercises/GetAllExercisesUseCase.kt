package nl.medify.patientuser.feature_exercises.domain.use_case.get_all_exercises

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.patientuser.feature_exercises.domain.model.Exercise
import nl.medify.patientuser.feature_exercises.domain.repository.ExerciseRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllExercisesUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {

    operator fun invoke(id: String): Flow<Resource<ArrayList<Exercise>>> = flow {
        try {
            emit(Resource.Loading())
            val temp = repository.getExerciseOfPatient(id)
            emit(Resource.Success(temp))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}