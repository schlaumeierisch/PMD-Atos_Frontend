package nl.medify.doctoruser.feature_login.domain.use_case.get_care_provider_by_email

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.doctoruser.feature_login.domain.repository.LoginRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCareProviderByEmailUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke(email: String): Flow<Resource<DoctorOrCareProvider>> = flow {
        try {
            emit(Resource.Loading())
            val careProvider = repository.getCareProviderByEmail(email)
            emit(Resource.Success(careProvider))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}