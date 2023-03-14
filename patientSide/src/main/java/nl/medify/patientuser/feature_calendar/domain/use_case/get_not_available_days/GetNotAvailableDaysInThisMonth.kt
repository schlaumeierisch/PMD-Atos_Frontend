package nl.medify.patientuser.feature_calendar.domain.use_case.get_not_available_days

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.patientuser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class GetNotAvailableDaysInThisMonth @Inject constructor(
    private val repository: CalendarRepository
) {

    operator fun invoke(
        yearNumber: Int,
        monthNumber: Int,
        gpId: String
    ): Flow<Resource<List<LocalDate>>> = flow {
        try {
            emit(Resource.Loading())

            val notAvailableDaysInThisMonth = repository.getNotAvailableDaysInThisMonth(
                yearNumber,
                monthNumber,
                gpId
            ).map { LocalDate.parse(it) }

            emit(Resource.Success(notAvailableDaysInThisMonth))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}