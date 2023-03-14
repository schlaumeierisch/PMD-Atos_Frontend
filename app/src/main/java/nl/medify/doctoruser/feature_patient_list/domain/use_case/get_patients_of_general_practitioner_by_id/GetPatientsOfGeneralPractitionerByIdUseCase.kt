package nl.medify.doctoruser.feature_patient_list.domain.use_case.get_patients_of_general_practitioner_by_id

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_patient_list.domain.repository.PatientListRepository
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPatientsOfGeneralPractitionerByIdUseCase @Inject constructor(
    private val repository: PatientListRepository
) {
    operator fun invoke(id: String): Flow<Resource<List<Patient>>> = flow {
        try {
            emit(Resource.Loading())
            val patients = repository.getPatientsOfGeneralPractitionerById(id)
            emit(Resource.Success(patients))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}