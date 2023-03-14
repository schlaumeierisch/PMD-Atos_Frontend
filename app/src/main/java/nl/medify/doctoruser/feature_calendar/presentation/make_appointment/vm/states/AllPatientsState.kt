package nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.states

import nl.medify.utilities.data_classes.Patient

data class AllPatientsState(
    val isLoading: Boolean = false,
    val allPatientsOfDoctorOrCareProvider: ArrayList<Patient>? = null,
    val error: String = ""
)