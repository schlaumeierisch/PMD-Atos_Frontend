package nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.states

import com.google.android.gms.maps.model.LatLng

data class GeoCodesState(
    val isLoading: Boolean = false,
    val geoCode: LatLng? = null,
    val error: String = ""
)