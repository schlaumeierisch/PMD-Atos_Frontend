package nl.medify.patientuser.feature_permission_linking.feature_care_providers.presentation.vm

import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider

data class CareProviderListState (
    val isLoading: Boolean = false,
    val careProviderList: List<DoctorOrCareProvider> = emptyList(),
    val error: String = ""
)