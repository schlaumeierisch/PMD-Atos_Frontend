package nl.medify.patientuser.feature_login.domain.use_case.get_patient_by_email

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.patientuser.feature_login.domain.repository.LoginRepository
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPatientByEmailUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke(email: String): Flow<Resource<Patient>> = flow {
        try {
            emit(Resource.Loading())
            val patient = repository.getPatientByEmail(email)
            emit(Resource.Success(patient))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}