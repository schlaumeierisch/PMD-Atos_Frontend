package nl.medify.patientuser.feature_notes.presentation

import NoteListAdapter
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
import nl.medify.patientuser.R
import nl.medify.patientuser.databinding.FragmentNoteListBinding
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel
import nl.medify.patientuser.feature_notes.presentation.vm.NoteViewModel
import nl.medify.utilities.data_classes.Note

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private var notes = listOf<Note>()
    private val state: Boolean = true
    private lateinit var noteListAdapter: NoteListAdapter

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        // Set page title
        binding.container.tvTitle.text = getString(R.string.notes)

        // Set navigation(s)
        binding.container.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_medicalRecordFragment)
        }
        binding.btnAddExercise.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }

        // Add listener for search/filter bar
        binding.etFilterNoteList.addTextChangedListener(textWatcher)

        // Getting data from the database and adding it into the recycleView
        noteViewModel.getAllNotes(loginViewModel.loggedInPatient!!.medicalRecordId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteViewModel.noteList.collect {
                    noteListAdapter = NoteListAdapter(it.noteList, state) { _, _ ->
                        // nothing needs to happen here
                    }
                    notes = it.noteList
                    initRv()
                }
            }
        }
    }

    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, and initializing the adapter
        binding.rvNoteList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvNoteList.adapter = noteListAdapter
        binding.rvNoteList.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Keep this empty.
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Keep this empty.
        }

        // Everytime after the user inputted text into the search bar, this method is being called.
        override fun afterTextChanged(p0: Editable?) {
            // The list if being filtered immediately after input from the user
            filter(p0.toString())
        }
    }

    // Here a new arrayList is created where the new notes will be stored in, that fit in the filter.
    private fun filter(inputText: String) {
        val filteredList = arrayListOf<Note>()

        for (note in notes) {
            if (note.title.lowercase().contains(inputText.lowercase())) {
                filteredList.add(note)
            } else if (note.date.contains(inputText)) {
                filteredList.add(note)
            }
        }

        // This new arraylist will be swapped with the current list in the recycleView
        noteListAdapter.filterList(filteredList)
    }
}