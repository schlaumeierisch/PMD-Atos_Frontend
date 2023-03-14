package nl.medify.doctoruser.feature_notes.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentNoteListBinding
import nl.medify.doctoruser.feature_login.domain.model.UserRole
import nl.medify.doctoruser.feature_login.presentation.vm.LoginViewModel
import nl.medify.doctoruser.feature_notes.presentation.vm.NoteViewModel
import nl.medify.doctoruser.feature_patient_list.presentation.vm.PatientListViewModel
import nl.medify.utilities.data_classes.Note
import nl.medify.utilities.data_classes.Patient

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteListAdapter: NoteListAdapter
    private val noteViewModel: NoteViewModel by viewModels()
    private var notes = listOf<Note>()

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val patientListViewModel: PatientListViewModel by activityViewModels()
    private lateinit var selectedPatient: Patient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedPatient = patientListViewModel.selectedPatient!!
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        // set page title
        binding.header.tvTitle.setText(R.string.notes)

        // set navigation(s)
        binding.header.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_medicalRecordFragment)
        }

        binding.btnAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }

        // add listener for search/filter bar
        binding.etFilterNoteList.addTextChangedListener(textWatcher)

        // Get the data to fill the recyclerview
        if (loginViewModel.loggedInUserRole == UserRole.GENERAL_PRACTITIONER) {
            noteViewModel.getAllNotes(selectedPatient.medicalRecordId)
        } else {
            noteViewModel.getNotesOfMedicalRecord(
                selectedPatient.medicalRecordId,
                loginViewModel.loggedInUser!!.id
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteViewModel.noteList.collect {
                    noteListAdapter = NoteListAdapter(it.noteList)
                    notes = it.noteList
                    initRv()
                }
            }
        }
    }

    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvNoteList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvNoteList.adapter = noteListAdapter
        binding.rvNoteList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        //after the user inputted text into the search bar this method is being called.
        override fun afterTextChanged(p0: Editable?) {
            filter(p0.toString())
        }
    }

    //here a new arrayList is created where the new notes will be stored, that fit in the filter.
    private fun filter(inputText: String) {
        val filteredList = arrayListOf<Note>()

        for (note in notes) {
            if (note.title.lowercase().contains(inputText.lowercase())) {
                filteredList.add(note)
            }
        }
        noteListAdapter.filterList(filteredList)
    }
}