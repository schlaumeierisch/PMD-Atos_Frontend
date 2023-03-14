package nl.medify.patientuser.feature_calendar.domain.use_case.get_available_timeslots

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.patientuser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.utilities.data_classes.TimeSlot
import nl.medify.utilities.util.Resource
import nl.medify.utilities.util.toTimeSlots
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAvailableTimeSlotsOfParticularDay @Inject constructor(
    private val repository: CalendarRepository
) {

    operator fun invoke(
        yearNumber: Int,
        monthNumber: Int,
        monthDay: Int,
        gpId: String
    ): Flow<Resource<List<TimeSlot>>> = flow {
        try {
            emit(Resource.Loading())

            val availableTimeSlotsOfParticularDay = repository.getAvailableTimeSlotsOfParticularDay(
                yearNumber,
                monthNumber,
                monthDay,
                gpId
            ).toTimeSlots()

            emit(Resource.Success(availableTimeSlotsOfParticularDay))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}