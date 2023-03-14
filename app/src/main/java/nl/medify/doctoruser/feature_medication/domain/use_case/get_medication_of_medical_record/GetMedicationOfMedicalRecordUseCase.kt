package nl.medify.doctoruser.feature_medication.domain.use_case.get_medication_of_medical_record

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_medication.domain.model.Medication
import nl.medify.doctoruser.feature_medication.domain.repository.MedicationRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMedicationOfMedicalRecordUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(mrId: String, cpId: String): Flow<Resource<ArrayList<Medication>>> = flow {
        try {
            emit(Resource.Loading())
            val temp = repository.getMedicationOfMedicalRecord(mrId, cpId)
            emit(Resource.Success(temp))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}