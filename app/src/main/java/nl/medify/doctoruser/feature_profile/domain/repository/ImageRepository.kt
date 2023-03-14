package nl.medify.doctoruser.feature_profile.domain.repository

import nl.medify.doctoruser.feature_profile.domain.model.Image

interface ImageRepository {
    suspend fun getImageByPatientId(id: String): ArrayList<Image>
}