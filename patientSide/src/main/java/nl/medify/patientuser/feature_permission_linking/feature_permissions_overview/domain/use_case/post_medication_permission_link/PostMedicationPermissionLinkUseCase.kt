package nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.post_medication_permission_link

import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.repository.PermissionsRepository
import javax.inject.Inject

class PostMedicationPermissionLinkUseCase @Inject constructor(
    private val repository: PermissionsRepository
) {

    suspend operator fun invoke(medicationId: String, careProviderId: String, validDate: String) {
        repository.createMedicationPermissionLink(medicationId, careProviderId, validDate)
    }

}