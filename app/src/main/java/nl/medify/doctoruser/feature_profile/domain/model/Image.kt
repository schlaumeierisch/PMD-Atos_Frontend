package nl.medify.doctoruser.feature_profile.domain.model

import com.google.gson.annotations.SerializedName

class Image(
    @SerializedName("patientId") val patientId: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("image") val image: String,
)
