package nl.medify.doctoruser.feature_profile.data.repository


import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_profile.domain.model.Image
import nl.medify.doctoruser.feature_profile.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val api: MedifyAPIService
) : ImageRepository {

    override suspend fun getImageByPatientId(id: String): ArrayList<Image> {
        return api.getImageByPatientId(id)
    }

}