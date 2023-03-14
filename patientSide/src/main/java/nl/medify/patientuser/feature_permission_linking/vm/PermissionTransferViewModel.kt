package nl.medify.patientuser.feature_permission_linking.vm
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.patientuser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.patientuser.feature_exercises.domain.model.Exercise
import nl.medify.patientuser.feature_medication.domain.model.Medication
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.use_case.PermissionsUseCases
import nl.medify.utilities.data_classes.Note
import javax.inject.Inject

@HiltViewModel
class PermissionTransferViewModel @Inject constructor(
    private val permissionsUseCases: PermissionsUseCases
) : ViewModel() {

    var visitedCareProviderPage: Boolean = false
    var visitedDateConfirmationPage: Boolean = false

    var listOfMedication: ArrayList<Medication> = arrayListOf()
        private set

    fun saveListOfMedication(medication: ArrayList<Medication>) {
        listOfMedication.addAll(medication)
    }
    fun deleteListOfMedication(medication : List<Medication>){
        listOfMedication.removeAll(medication.toSet())
    }

    var listOfDiagnosis: ArrayList<Diagnosis> = arrayListOf()
        private set

    fun saveListOfDiagnosis(diagnosis: ArrayList<Diagnosis>) {
        listOfDiagnosis.addAll(diagnosis)
    }
    fun deleteListOfDiagnosis(diagnosis: List<Diagnosis>){
        listOfDiagnosis.removeAll(diagnosis.toSet())
    }

    var listOfExercises: ArrayList<Exercise> = arrayListOf()
        private set

    fun saveListOfExercises(exercises: ArrayList<Exercise>) {
        listOfExercises.addAll(exercises)
    }
    fun deleteListOfExercises(exercises: List<Exercise>){
        listOfExercises.removeAll(exercises.toSet())
    }

    var listOfNotes: ArrayList<Note> = arrayListOf()
        private set

    fun saveListOfNotes(notes: ArrayList<Note>) {
        listOfNotes.addAll(notes)
    }
    fun deleteListOfNotes(notes: List<Note>){
        listOfNotes.removeAll(notes.toSet())
    }

    var listOfCareProviders: ArrayList<DoctorOrCareProvider> = arrayListOf()
        private set

    fun saveListOfCareProviders(careProviders: ArrayList<DoctorOrCareProvider>) {
        listOfCareProviders.addAll(careProviders)
    }

    fun deleteListOfCareProviders(careProviders: ArrayList<DoctorOrCareProvider>) {
        listOfCareProviders.removeAll(careProviders.toSet())
    }

    var selectedDate: String? = null
        private set

    fun saveSelectedDate(date: String) {
        selectedDate = date
    }

    fun postMedicationPermissionLink(
        medicationId: String,
        careProviderId: String,
        validDate: String
    ) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        permissionsUseCases.postMedicationPermissionLinkUseCase(
            medicationId,
            careProviderId,
            validDate
        )
    }

    fun postDiagnosisPermissionLink(
        diagnosisId: String,
        careProviderId: String,
        validDate: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        permissionsUseCases.postDiagnosisPermissionLinkUseCase(
            diagnosisId,
            careProviderId,
            validDate
        )
    }

    fun postExercisePermissionLink(exerciseId: String, careProviderId: String, validDate: String) =
        viewModelScope.launch(Dispatchers.IO) {
            permissionsUseCases.postExercisePermissionLinkUseCase(
                exerciseId,
                careProviderId,
                validDate
            )
        }

    fun postNotePermissionLink(noteId: String, careProviderId: String, validDate: String) =
        viewModelScope.launch(Dispatchers.IO) {
            permissionsUseCases.postNotePermissionLinkUseCase(
                noteId,
                careProviderId,
                validDate
            )
        }

}