package nl.medify.utilities.data_classes

import com.google.gson.annotations.SerializedName

class Note(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String
)