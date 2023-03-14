package nl.medify.patientuser.feature_medication.presentation.vm

import nl.medify.patientuser.feature_medication.domain.model.Medication

data class MedicationListState(
    val isLoading: Boolean = false,
    val medicationList: List<Medication> = emptyList(),
    val error: String = ""
)