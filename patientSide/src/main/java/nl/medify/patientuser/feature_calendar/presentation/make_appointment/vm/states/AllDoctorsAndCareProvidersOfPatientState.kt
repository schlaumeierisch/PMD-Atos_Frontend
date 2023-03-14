package nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.states

import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider

data class AllDoctorsAndCareProvidersOfPatientState(
    val isLoading: Boolean = false,
    val allDoctorsAndCareProvidersOfPatient: ArrayList<DoctorOrCareProvider>? = null,
    val error: String = ""
)