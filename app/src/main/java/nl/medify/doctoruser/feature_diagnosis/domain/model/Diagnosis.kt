package nl.medify.doctoruser.feature_diagnosis.domain.model

import com.google.gson.annotations.SerializedName

class Diagnosis(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("diagnosisType") val diagnosisType: String,
    @SerializedName("dateDiagnosed") val dateDiagnosed: String,
    @SerializedName("cause") val cause: String,
    @SerializedName("treatment") val treatment: String,
    @SerializedName("advice") val advice: String,
    @SerializedName("medicalRecordId") val medicalRecordId: String
)