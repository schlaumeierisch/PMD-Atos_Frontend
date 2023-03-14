package nl.medify.doctoruser.feature_diagnosis.domain.use_case.get_all_diagnoses

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.doctoruser.feature_diagnosis.domain.repository.DiagnosisRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllDiagnosesUseCase @Inject constructor(
    private val repository: DiagnosisRepository
) {
    operator fun invoke(id: String): Flow<Resource<ArrayList<Diagnosis>>> = flow {
        try {
            emit(Resource.Loading())
            val temp = repository.getDiagnosisOfPatient(id)
            emit(Resource.Success(temp))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}