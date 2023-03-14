package nl.medify.doctoruser.feature_patient_list.presentation.vm

import nl.medify.utilities.data_classes.Patient

data class PatientListState(
    val isLoading: Boolean = false,
    val patientList: List<Patient> = emptyList(),
    val error: String = ""
)