package nl.medify.patientuser.feature_calendar.domain.use_case.get_geo_codes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.medify.patientuser.feature_calendar.domain.model.GeoCode
import nl.medify.patientuser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.utilities.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetGeoCodesUseCase @Inject constructor(
    private val repository: CalendarRepository
) {

    operator fun invoke(city: String, street: String, postalcode: String): Flow<Resource<ArrayList<GeoCode>>> = flow {
        try {
            emit(Resource.Loading())

            val geocodes = repository.getGeoCodes(city, street, postalcode)

            emit(Resource.Success(geocodes))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection. "))
        }
    }
}