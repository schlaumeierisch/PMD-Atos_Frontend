package nl.medify.doctoruser.feature_calendar.domain.model

import nl.medify.utilities.data_classes.AddressDTO

data class DoctorOrCareProvider(
    val id: String,
    val firstName: String,
    val lastName: String,
    val addressDTO: AddressDTO,
    val phoneNumber: String,
    val specialism: String? = null,
    val email: String
)