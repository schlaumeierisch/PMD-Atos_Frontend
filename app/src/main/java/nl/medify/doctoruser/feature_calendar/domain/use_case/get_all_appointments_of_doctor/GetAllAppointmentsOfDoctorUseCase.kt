package nl.medify.doctoruser.feature_calendar.domain.use_case.get_all_appointments_of_doctor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.doctoruser.feature_calendar.domain.model.ShowAppointment
import nl.medify.doctoruser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

class GetAllAppointmentsOfDoctorUseCase @Inject constructor(
    private val repository: CalendarRepository
) {

    private var temp = ArrayList<ShowAppointment>()

    operator fun invoke(id: String): Flow<Resource<ArrayList<ShowAppointment>>> = flow {
        try {
            temp.clear()
            emit(Resource.Loading())

            repository.getAllAppointmentsOfDoctorByID(id).onEach { appointment ->
                appointment.gpId?.let {
                    val tempResult = repository.getPatientByID(appointment.patientId)
                    val tempResult2 = repository.getDoctorByID(it)

                    temp.add(
                        ShowAppointment(
                            appointmentID = appointment.id,
                            name = "${tempResult.firstName} ${tempResult.lastName}",
                            dateAndTime = LocalDateTime.parse(appointment.dateTime),
                            address = "${tempResult2.addressDTO.city}, ${tempResult2.addressDTO.street}, ${tempResult2.addressDTO.zip}",
                            reasonForVisit = appointment.reason
                        )
                    )
                }
            }

            emit(Resource.Success(temp))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}