package nl.medify.doctoruser.feature_medication.domain.model

import com.google.gson.annotations.SerializedName

data class Medication(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("medicalRecordId") val medicalRecordId: String
)
