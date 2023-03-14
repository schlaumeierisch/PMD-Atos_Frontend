package nl.medify.patientuser.feature_permission_linking.feature_medical_record.presentation

import NoteListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.medify.patientuser.R
import nl.medify.patientuser.databinding.FragmentPermissionsMedicalRecordBinding
import nl.medify.patientuser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.patientuser.feature_diagnosis.presentation.DiagnosisListAdapter
import nl.medify.patientuser.feature_diagnosis.presentation.vm.DiagnosisViewModel
import nl.medify.patientuser.feature_exercises.domain.model.Exercise
import nl.medify.patientuser.feature_exercises.presentation.ExerciseListAdapter
import nl.medify.patientuser.feature_exercises.presentation.vm.ExerciseViewModel
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel
import nl.medify.patientuser.feature_medication.domain.model.Medication
import nl.medify.patientuser.feature_medication.presentation.MedicationListAdapter
import nl.medify.patientuser.feature_medication.presentation.vm.MedicationViewModel
import nl.medify.patientuser.feature_notes.presentation.vm.NoteViewModel
import nl.medify.patientuser.feature_permission_linking.vm.PermissionTransferViewModel
import nl.medify.utilities.data_classes.Note

@AndroidEntryPoint
class PermissionsMedicalRecordFragment: Fragment() {
    private var _binding: FragmentPermissionsMedicalRecordBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by activityViewModels()

    private val medicationViewModel : MedicationViewModel by activityViewModels()
    private val diagnosisViewModel : DiagnosisViewModel by activityViewModels()
    private val exercisesViewModel : ExerciseViewModel by activityViewModels()
    private val noteViewModel : NoteViewModel by activityViewModels()

    // In this viewModel are arrayLists stored that are being used to transfer data from one
    // fragment to another fragment.
    private val transferDataViewModel : PermissionTransferViewModel by activityViewModels()

    private var medications = listOf<Medication>()
    private var diagnosis = listOf<Diagnosis>()
    private var exercises = listOf<Exercise>()
    private var notes = listOf<Note>()

    private var medicationToShareWithCareProvider = arrayListOf<Medication>()
    private var diagnosisToShareWithCareProvider = arrayListOf<Diagnosis>()
    private var exercisesToShareWithCareProvider = arrayListOf<Exercise>()
    private var notesToShareWithCareProvider = arrayListOf<Note>()

    // The adapters that are used in this fragment have all two states, because they are used in
    // different fragments with different layouts. So based on the boolean, the adapter decides
    // which fragment to use.
    private val stateOfAdapterWhichFragmentItIsUsing: Boolean = false

    private var shareMedicationMethod : Int = 0
    private var shareDiagnosisMethod : Int = 0
    private var shareExercisesMethod : Int = 0
    private var shareNotesMethod : Int = 0

    private val shareSpecificDataMethod : Int = 2
    private val shareAllDataMethod : Int = 1
    private val shareNoDataMethod : Int = 0

    private lateinit var medicationListPermissionAdapter : MedicationListAdapter
    private lateinit var diagnosisListPermissionAdapter : DiagnosisListAdapter
    private lateinit var exerciseListPermissionAdapter : ExerciseListAdapter
    private lateinit var noteListPermissionAdapter : NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionsMedicalRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Every time the users enters the fragment, everything is being reset to the original state.
        resetDataAndUI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set page title
        binding.container.tvTitle.setText(nl.medify.utilities.R.string.step_one_four)

        // set navigation(s)
        binding.container.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // navigating to step two with all selected data
        binding.btnGoToStepTwo.setOnClickListener {
            storingDataIntoLocalArray()
        }

        initViews()
        spinnerMedication()
        spinnerDiagnosis()
        spinnerExercises()
        spinnerNotes()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews(){
        // If the user came back from step 2 of the permissions, the user will be informed that all
        // changes the user had made in this fragment have been reset.
        informingUserAboutDataReset()

        medicationViewModel.getMedicationOfPatient(loginViewModel.loggedInPatient!!.medicalRecordId)
        // Initialize the recycler view with a linear layout manager, adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                medicationViewModel.medicationList.collect {
                    medicationListPermissionAdapter = MedicationListAdapter(it.medicationList, stateOfAdapterWhichFragmentItIsUsing){medication, cbIsSelected ->
                        checkBoxOnClickMedication(medication, cbIsSelected)
                    }
                    medications = it.medicationList
                    initRvMedication()
                }
            }
        }

        diagnosisViewModel.getAllDiagnoses(loginViewModel.loggedInPatient!!.medicalRecordId)
        // Initialize the recycler view with a linear layout manager, adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                diagnosisViewModel.diagnoseList.collect {
                    diagnosisListPermissionAdapter = DiagnosisListAdapter(it.diagnosisList, stateOfAdapterWhichFragmentItIsUsing){ diagnosis, cbIsSelected ->
                        checkBoxOnClickDiagnosis(diagnosis, cbIsSelected)
                    }
                    diagnosis = it.diagnosisList
                    initRvDiagnosis()
                }
            }
        }

        exercisesViewModel.getAllExercises(loginViewModel.loggedInPatient!!.medicalRecordId)
        // Initialize the recycler view with a linear layout manager, adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exercisesViewModel.exerciseList.collect {
                    exerciseListPermissionAdapter = ExerciseListAdapter( it.exerciseList, stateOfAdapterWhichFragmentItIsUsing){ exercise, cbIsSelected ->
                        checkBoxOnClickExercises(exercise, cbIsSelected)
                    }
                    exercises = it.exerciseList
                    initRvExercises()
                }
            }
        }

        noteViewModel.getAllNotes(loginViewModel.loggedInPatient!!.medicalRecordId)
        // Initialize the recycler view with a linear layout manager, adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteViewModel.noteList.collect {
                    noteListPermissionAdapter = NoteListAdapter(it.noteList, stateOfAdapterWhichFragmentItIsUsing){ note, cbIsSelected ->
                        checkBoxOnClickNotes(note, cbIsSelected)
                    }
                    notes = it.noteList
                    initRvNote()
                }
            }
        }
    }

    private fun initRvMedication(){
        binding.rvMedication.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvMedication.adapter = medicationListPermissionAdapter
        binding.rvMedication.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initRvDiagnosis(){
        binding.rvDiagnosis.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvDiagnosis.adapter = diagnosisListPermissionAdapter
        binding.rvDiagnosis.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initRvExercises(){
        binding.rvExercise.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvExercise.adapter = exerciseListPermissionAdapter
        binding.rvExercise.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initRvNote(){
        binding.rvNotes.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvNotes.adapter = noteListPermissionAdapter
        binding.rvNotes.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun spinnerMedication(){
        // creating a spinner(top down bar) for selecting methods of sharing data of notes
        val adapter: ArrayAdapter<CharSequence> = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.data_sharing_options, android.R.layout.simple_spinner_item )
        } as ArrayAdapter<CharSequence>
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnrMedication.adapter = adapter
        binding.spnrMedication.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            // Here the sharingMethods variables get their values so later on we know in which way
            // the user wants to share data and we can make sure that process goes smoothly.
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    shareSpecificDataMethod -> {
                        shareMedicationMethod = shareSpecificDataMethod
                        binding.rvMedication.visibility = View.VISIBLE
                    }
                    shareAllDataMethod -> {
                        shareMedicationMethod = shareAllDataMethod
                        binding.rvMedication.visibility = View.GONE
                    }
                    else -> {
                        shareMedicationMethod = shareNoDataMethod
                        binding.rvMedication.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
    private fun spinnerDiagnosis(){
        // creating a spinner(top down bar) for selecting methods of sharing data of notes
        val adapter: ArrayAdapter<CharSequence> = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.data_sharing_options, android.R.layout.simple_spinner_item )
        } as ArrayAdapter<CharSequence>
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnrDiagnosis.adapter = adapter
        binding.spnrDiagnosis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            // Here the sharingMethods variables get their values so later on we know in which way
            // the user wants to share data and we can make sure that process goes smoothly.
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    shareSpecificDataMethod -> {
                        shareDiagnosisMethod = shareSpecificDataMethod
                        binding.rvDiagnosis.visibility = View.VISIBLE
                    }
                    shareAllDataMethod -> {
                        shareDiagnosisMethod = shareAllDataMethod
                        binding.rvDiagnosis.visibility = View.GONE
                    }
                    else -> {
                        shareDiagnosisMethod = shareNoDataMethod
                        binding.rvDiagnosis.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
    private fun spinnerExercises(){
        // creating a spinner(top down bar) for selecting methods of sharing data of notes
        val adapter: ArrayAdapter<CharSequence> = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.data_sharing_options, android.R.layout.simple_spinner_item )
        } as ArrayAdapter<CharSequence>
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnrExercises.adapter = adapter
        binding.spnrExercises.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            // Here the sharingMethods variables get their values so later on we know in which way
            // the user wants to share data and we can make sure that process goes smoothly.
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    shareSpecificDataMethod -> {
                        shareExercisesMethod = shareSpecificDataMethod
                        binding.rvExercise.visibility = View.VISIBLE
                    }
                    shareAllDataMethod -> {
                        shareExercisesMethod = shareAllDataMethod
                        binding.rvExercise.visibility = View.GONE
                    }
                    else -> {
                        shareExercisesMethod = shareNoDataMethod
                        binding.rvExercise.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
    private fun spinnerNotes(){
        // creating a spinner(top down bar) for selecting methods of sharing data of notes
        val adapter: ArrayAdapter<CharSequence> = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.data_sharing_options, android.R.layout.simple_spinner_item )
        } as ArrayAdapter<CharSequence>
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnrNotes.adapter = adapter
        binding.spnrNotes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            // Here the sharingMethods variables get their values so later on we know in which way
            // the user wants to share data and we can make sure that process goes smoothly.
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    shareSpecificDataMethod -> {
                        shareNotesMethod = shareSpecificDataMethod
                        binding.rvNotes.visibility = View.VISIBLE
                    }
                    shareAllDataMethod -> {
                        shareNotesMethod = shareAllDataMethod
                        binding.rvNotes.visibility = View.GONE
                    }
                    else -> {
                        shareNotesMethod = shareNoDataMethod
                        binding.rvNotes.visibility = View.GONE
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    // When a checkbox is being clicked this method gets the state of the checkbox and which item it
    // is. Based on that information the method decides to store the item in the list or remove it.
    private fun checkBoxOnClickMedication(medication: Medication, checkBoxState: Boolean){
        if(checkBoxState){
            medicationToShareWithCareProvider.add(medication)
        }else{
            medicationToShareWithCareProvider.remove(medication)
        }
    }

    // When a checkbox is being clicked this method gets the state of the checkbox and which item it
    // is. Based on that information the method decides to store the item in the list or remove it.
    private fun checkBoxOnClickDiagnosis(diagnosis: Diagnosis, checkBoxState: Boolean){
        if(checkBoxState){
            diagnosisToShareWithCareProvider.add(diagnosis)
        }else{
            diagnosisToShareWithCareProvider.remove(diagnosis)
        }
    }

    // When a checkbox is being clicked this method gets the state of the checkbox and which item it
    // is. Based on that information the method decides to store the item in the list or remove it.
    private fun checkBoxOnClickExercises(exercise: Exercise, checkBoxState: Boolean){
        if(checkBoxState){
            exercisesToShareWithCareProvider.add(exercise)
        }else{
            exercisesToShareWithCareProvider.remove(exercise)
        }
    }

    // When a checkbox is being clicked this method gets the state of the checkbox and which item it
    // is. Based on that information the method decides to store the item in the list or remove it.
    private fun checkBoxOnClickNotes(note : Note, checkBoxState : Boolean){
        if(checkBoxState) {
            notesToShareWithCareProvider.add(note)
        }else{
            notesToShareWithCareProvider.remove(note)
        }
    }

    private fun storingDataIntoLocalArray(){
        // saving arrayLists into viewModels
        when(shareMedicationMethod){
            shareSpecificDataMethod -> {
                // nothing needs to happen here, the arraylist won't be changed and will be transferred
                // to the next fragment (step 2).
            }
            shareAllDataMethod -> {
                medicationToShareWithCareProvider.removeAll(medications.toSet())
                medicationToShareWithCareProvider.addAll(medications)
            }
            shareNoDataMethod -> {
                medicationToShareWithCareProvider.removeAll(medications.toSet())
            }
        }
        when(shareDiagnosisMethod){
            shareSpecificDataMethod -> {
                // nothing needs to happen here, the arraylist won't be changed and will be transferred
                // to the next fragment (step 2).
            }
            shareAllDataMethod -> {
                diagnosisToShareWithCareProvider.removeAll(diagnosis.toSet())
                diagnosisToShareWithCareProvider.addAll(diagnosis)
            }
            shareNoDataMethod -> {
                diagnosisToShareWithCareProvider.removeAll(diagnosis.toSet())
            }
        }
        when(shareExercisesMethod){
            shareSpecificDataMethod -> {
                // nothing needs to happen here, the arraylist won't be changed and will be transferred
                // to the next fragment (step 2).
            }
            shareAllDataMethod -> {
                exercisesToShareWithCareProvider.removeAll(exercises.toSet())
                exercisesToShareWithCareProvider.addAll(exercises)
            }
            shareNoDataMethod -> {
                exercisesToShareWithCareProvider.removeAll(exercises.toSet())
            }
        }
        when(shareNotesMethod){
            shareSpecificDataMethod -> {
                // nothing needs to happen here, the arraylist won't be changed and will be transferred
                // to the next fragment (step 2).
            }
            shareAllDataMethod -> {
                notesToShareWithCareProvider.removeAll(notes.toSet())
                notesToShareWithCareProvider.addAll(notes)
            }
            shareNoDataMethod -> {
                notesToShareWithCareProvider.removeAll(notes.toSet())
            }
        }
        // If the user didn't select any data to share, the user will be notified about this, and
        // can't move on as long as the user doesn't select any data to share.
        if(checkIfAllSpinnersAreOnNoDataSharing()){
            Toast.makeText(
                activity,
                nl.medify.utilities.R.string.no_data_selected_warning, Toast.LENGTH_SHORT
            ).show()
        }else {
            navigateToNextStep()
        }
    }

    // Here is where the user is being navigated to the next step, included with putting all the
    // selected data in the arrayList(s) of the transferModelView.
    private fun navigateToNextStep(){
        transferDataViewModel.saveListOfMedication(medicationToShareWithCareProvider)
        transferDataViewModel.saveListOfDiagnosis(diagnosisToShareWithCareProvider)
        transferDataViewModel.saveListOfExercises(exercisesToShareWithCareProvider)
        transferDataViewModel.saveListOfNotes(notesToShareWithCareProvider)

        // Making sure the system understands that the user did not come from the careProviderPage.
        transferDataViewModel.visitedCareProviderPage = false
        findNavController().navigate(R.id.action_permissionsMedicalRecordFragment_to_careProvidersListFragment)
    }

    private fun checkIfAllSpinnersAreOnNoDataSharing() : Boolean {
        return (shareMedicationMethod == shareNoDataMethod && shareDiagnosisMethod == shareNoDataMethod
                && shareExercisesMethod == shareNoDataMethod && shareNotesMethod == shareNoDataMethod)
    }

    // resetting the arrayLists and the recycleView.
    private fun resetDataAndUI(){
        medicationToShareWithCareProvider.removeAll(medications.toSet())
        diagnosisToShareWithCareProvider.removeAll(diagnosis.toSet())
        exercisesToShareWithCareProvider.removeAll(exercises.toSet())
        notesToShareWithCareProvider.removeAll(notes.toSet())

        binding.rvMedication.visibility = View.GONE
        binding.rvDiagnosis.visibility = View.GONE
        binding.rvExercise.visibility = View.GONE
        binding.rvNotes.visibility = View.GONE

        binding.spnrMedication.setSelection(0)
        binding.spnrDiagnosis.setSelection(0)
        binding.spnrExercises.setSelection(0)
        binding.spnrNotes.setSelection(0)

        medicationToShareWithCareProvider.removeAll(medications.toSet())
        diagnosisToShareWithCareProvider.removeAll(diagnosis.toSet())
        exercisesToShareWithCareProvider.removeAll(exercises.toSet())
        notesToShareWithCareProvider.removeAll(notes.toSet())

        transferDataViewModel.deleteListOfMedication(medications)
        transferDataViewModel.deleteListOfDiagnosis(diagnosis)
        transferDataViewModel.deleteListOfExercises(exercises)
        transferDataViewModel.deleteListOfNotes(notes)
    }

    // When the user comes from the careProviderPage, the user will be informed that the last
    // adjustments to the data have been reset.
    private fun informingUserAboutDataReset(){
        // This boolean in the viewModel keeps track when the user came from the careProviderPage
        // and when not. If user enters the careProvideFragment the boolean is set true. When the
        // user leaves step 1, or moves on to the next step from careProviderFragment, the boolean
        // is set false.
        if(transferDataViewModel.visitedCareProviderPage){
            binding.txtErrorPreventionMessage.visibility = View.VISIBLE
        }else{
            binding.txtErrorPreventionMessage.visibility = View.GONE
        }
    }
}