package nl.medify.doctoruser.feature_diagnosis.presentation.vm

import nl.medify.doctoruser.feature_diagnosis.domain.model.Diagnosis

data class DiagnosisListState(
    val isLoading: Boolean = false,
    val diagnosisList: List<Diagnosis> = emptyList(),
    val error: String = ""
)