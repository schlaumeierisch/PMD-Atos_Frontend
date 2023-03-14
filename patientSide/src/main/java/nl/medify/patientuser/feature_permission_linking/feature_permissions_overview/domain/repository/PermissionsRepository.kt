package nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.repository

interface PermissionsRepository {

    suspend fun createMedicationPermissionLink(medicationId: String, careProviderId: String, validDate: String)

    suspend fun createDiagnosisPermissionLink(diagnosisId: String, careProviderId: String, validDate: String)

    suspend fun createExercisePermissionLink(exerciseId: String, careProviderId: String, validDate: String)

    suspend fun createNotePermissionLink(noteId: String, careProviderId: String, validDate: String)
}