package nl.medify.doctoruser.feature_calendar.domain.use_case.get_all_patients_of_doctor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllPatientsOfDoctorUseCase @Inject constructor(
    private val repository: CalendarRepository
) {

    operator fun invoke(id: String): Flow<Resource<ArrayList<Patient>>> = flow {
        try {
            emit(Resource.Loading())

            val allPatientsOfDoctor = repository.getAllPatientsOfDoctor(id)

            emit(Resource.Success(allPatientsOfDoctor))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}