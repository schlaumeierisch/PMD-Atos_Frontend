package nl.medify.doctoruser.feature_exercises.domain.use_case.get_exercises_of_medical_record

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_exercises.domain.model.Exercise
import nl.medify.doctoruser.feature_exercises.domain.repository.ExerciseRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetExercisesOfMedicalRecordUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {

    operator fun invoke(mrId: String, cpId: String): Flow<Resource<ArrayList<Exercise>>> = flow {
        try {
            emit(Resource.Loading())
            val temp = repository.getExercisesOfMedicalRecord(mrId, cpId)
            emit(Resource.Success(temp))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}