package nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.post_note_permission_link

import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.repository.PermissionsRepository
import javax.inject.Inject

class PostNotePermissionLinkUseCase @Inject constructor(
    private val repository: PermissionsRepository
) {

    suspend operator fun invoke(noteId: String, careProviderId: String, validDate: String) {
        repository.createNotePermissionLink(noteId, careProviderId, validDate)
    }
}