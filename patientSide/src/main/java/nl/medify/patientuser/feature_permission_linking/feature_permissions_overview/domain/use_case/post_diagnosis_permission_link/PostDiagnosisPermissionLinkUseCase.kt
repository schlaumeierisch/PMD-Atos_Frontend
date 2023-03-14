package nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.post_diagnosis_permission_link

import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.repository.PermissionsRepository
import javax.inject.Inject

class PostDiagnosisPermissionLinkUseCase @Inject constructor(
    private val repository: PermissionsRepository
) {

    suspend operator fun invoke(diagnosisId: String, careProviderId: String, validDate: String) {
        repository.createDiagnosisPermissionLink(diagnosisId, careProviderId, validDate)
    }
}