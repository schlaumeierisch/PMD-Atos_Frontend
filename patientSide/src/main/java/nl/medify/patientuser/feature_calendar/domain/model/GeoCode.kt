package nl.medify.patientuser.feature_calendar.domain.model

data class GeoCode(
    val boundingbox: List<String>,
    val category: String,
    val display_name: String,
    val importance: Double,
    val lat: String,
    val licence: String,
    val lon: String,
    val osm_id: Long,
    val osm_type: String,
    val place_id: Int,
    val place_rank: Int,
    val type: String
)