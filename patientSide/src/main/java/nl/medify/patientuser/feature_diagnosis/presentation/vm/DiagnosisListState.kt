package nl.medify.patientuser.feature_diagnosis.presentation.vm

import nl.medify.patientuser.feature_diagnosis.domain.model.Diagnosis

data class DiagnosisListState(
    val isLoading: Boolean = false,
    val diagnosisList: List<Diagnosis> = emptyList(),
    val error: String = ""
)