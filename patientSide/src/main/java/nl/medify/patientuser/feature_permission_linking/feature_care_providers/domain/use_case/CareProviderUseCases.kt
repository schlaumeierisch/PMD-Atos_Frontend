package nl.medify.patientuser.feature_permission_linking.feature_care_providers.domain.use_case

import nl.medify.patientuser.feature_permission_linking.feature_care_providers.domain.use_case.get_all_care_providers.GetAllCareProvidersUseCase

data class CareProviderUseCases (
    val getAllCareProvidersUseCase: GetAllCareProvidersUseCase,
        )