package nl.medify.patientuser.feature_calendar.domain.use_case.get_all_appointments_of_patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.patientuser.feature_calendar.domain.model.ShowAppointment
import nl.medify.patientuser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

class GetAllAppointmentsOfPatientUseCase @Inject constructor(
    private val repository: CalendarRepository
) {

    private var temp = ArrayList<ShowAppointment>()

    operator fun invoke(id: String): Flow<Resource<ArrayList<ShowAppointment>>> = flow {
        try {
            temp.clear()
            emit(Resource.Loading())

            val tempp = repository.getAllAppointmentsOfPatientByID(id)

            tempp.onEach { appointment ->
                if (!appointment.gpId.isNullOrEmpty()) {
                    val tempResult = repository.getDoctorByID(appointment.gpId)
                    temp.add(
                        ShowAppointment(
                            appointmentID = appointment.id,
                            name = "${tempResult.firstName} ${tempResult.lastName}",
                            dateAndTime = LocalDateTime.parse(appointment.dateTime),
                            address = "${tempResult.addressDTO.city}, ${tempResult.addressDTO.street}, ${tempResult.addressDTO.zip}",
                            reasonForVisit = appointment.reason
                        )
                    )
                } else if (!appointment.cpId.isNullOrEmpty()) {
                    val tempResult = repository.getCareProviderByID(appointment.cpId)
                    temp.add(
                        ShowAppointment(
                            appointmentID = appointment.id,
                            name = "${tempResult.firstName} ${tempResult.lastName}",
                            dateAndTime = LocalDateTime.parse(appointment.dateTime),
                            address = "${tempResult.addressDTO.city}, ${tempResult.addressDTO.street}, ${tempResult.addressDTO.zip}",
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