package nl.medify.doctoruser.feature_profile.domain.use_case.get_image_by_patient_id

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_profile.domain.model.Image
import nl.medify.doctoruser.feature_profile.domain.repository.ImageRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetImageByPatientIdUseCase @Inject constructor(
    private val repository: ImageRepository
) {

    operator fun invoke(id: String): Flow<Resource<ArrayList<Image>>> = flow {
        try {
            emit(Resource.Loading())
            val temp = repository.getImageByPatientId(id)
            emit(Resource.Success(temp))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}