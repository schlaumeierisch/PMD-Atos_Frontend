package nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.post_exercise_permission_link

import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.repository.PermissionsRepository
import javax.inject.Inject

class PostExercisePermissionLinkUseCase @Inject constructor(
    private val repository: PermissionsRepository
) {
    suspend operator fun invoke(exerciseId: String, careProviderId: String, validDate: String) {
        repository.createExercisePermissionLink(exerciseId, careProviderId, validDate)
    }
}