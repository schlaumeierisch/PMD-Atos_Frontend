package nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.data.repository

import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.repository.PermissionsRepository
import javax.inject.Inject

class PermissionsRepositoryImpl @Inject constructor(
    private val api: PatientAPIService
) : PermissionsRepository {
    override suspend fun createMedicationPermissionLink(
        medicationId: String,
        careProviderId: String,
        validDate: String
    ) {
        return api.createMedicationPermission(medicationId, careProviderId,validDate)
    }

    override suspend fun createDiagnosisPermissionLink(
        diagnosisId: String,
        careProviderId: String,
        validDate: String
    ) {
        return api.createDiagnosisPermission(diagnosisId, careProviderId, validDate)
    }

    override suspend fun createExercisePermissionLink(
        exerciseId: String,
        careProviderId: String,
        validDate: String
    ) {
        return api.createExercisePermissionPermission(exerciseId, careProviderId, validDate)
    }

    override suspend fun createNotePermissionLink(
        noteId: String,
        careProviderId: String,
        validDate: String
    ) {
        return api.createNotePermission(noteId, careProviderId, validDate)


    }
}