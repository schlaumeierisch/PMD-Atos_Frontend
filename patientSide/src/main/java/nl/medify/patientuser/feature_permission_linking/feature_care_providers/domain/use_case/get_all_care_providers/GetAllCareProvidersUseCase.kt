package nl.medify.patientuser.feature_permission_linking.feature_care_providers.domain.use_case.get_all_care_providers

import kotlinx.coroutines.flow.flow
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.patientuser.feature_permission_linking.feature_care_providers.domain.repository.CareProviderRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllCareProvidersUseCase @Inject constructor(
    private val repository: CareProviderRepository
) {

    operator fun invoke(id: String): kotlinx.coroutines.flow.Flow<Resource<ArrayList<DoctorOrCareProvider>>> = flow {
        try {
            emit(Resource.Loading())
            val temp = repository.getCareProviderOfPatient(id)
            emit(Resource.Success(temp))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }


    }
}