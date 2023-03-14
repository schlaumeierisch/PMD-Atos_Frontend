package nl.medify.utilities.data_classes

data class Patient(
    val addressDTO: AddressDTO,
    val birthDate: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val gpId: String,
    val id: String,
    val lastName: String,
    val medicalRecordId: String,
    val phoneNumber: String,
    val usingApp: Boolean
)