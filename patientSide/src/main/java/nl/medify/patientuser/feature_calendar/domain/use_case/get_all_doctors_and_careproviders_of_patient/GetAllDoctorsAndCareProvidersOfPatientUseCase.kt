package nl.medify.patientuser.feature_calendar.domain.use_case.get_all_doctors_and_careproviders_of_patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.patientuser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllDoctorsAndCareProvidersOfPatientUseCase @Inject constructor(
    private val repository: CalendarRepository
) {

    operator fun invoke(id: String): Flow<Resource<ArrayList<DoctorOrCareProvider>>> = flow {
        try {
            emit(Resource.Loading())

            val getDoctorIDOfPatient = repository.getPatientByID(id)
            val getDoctorInfoOfPatient = repository.getDoctorByID(getDoctorIDOfPatient.gpId)
            val allCareProvidersOfPatientPlusDoctor = repository.getAllCareProvidersOfPatientByID(id)
            allCareProvidersOfPatientPlusDoctor.add(getDoctorInfoOfPatient)

            emit(Resource.Success(allCareProvidersOfPatientPlusDoctor))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}