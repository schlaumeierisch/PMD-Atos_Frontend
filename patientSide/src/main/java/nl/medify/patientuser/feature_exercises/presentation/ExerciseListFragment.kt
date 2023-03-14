package nl.medify.patientuser.feature_exercises.presentation

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
import nl.medify.patientuser.databinding.FragmentExerciseListBinding
import nl.medify.patientuser.feature_exercises.domain.model.Exercise
import nl.medify.patientuser.feature_exercises.presentation.vm.ExerciseViewModel
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel

@AndroidEntryPoint
class ExerciseListFragment : Fragment() {

    private var _binding: FragmentExerciseListBinding? = null
    private val binding get() = _binding!!

    private var exercises = listOf<Exercise>()
    private val state: Boolean = true
    private lateinit var exerciseAdapter: ExerciseListAdapter

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val exerciseViewModel: ExerciseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseListBinding.inflate(inflater, container, false)
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
        // set page title
        binding.container.tvTitle.setText(R.string.exercises)

        // set navigation(s)
        binding.container.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseListFragment_to_medicalRecordFragment)
        }

        // add listener for search/filter bar
        binding.etFilterExerciseList.addTextChangedListener(textWatcher)

        // Getting data from the database and adding it into the recycleView
        exerciseViewModel.getAllExercises(loginViewModel.loggedInPatient!!.medicalRecordId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseViewModel.exerciseList.collect {
                    exerciseAdapter = ExerciseListAdapter(it.exerciseList, state) { _, _ ->
                        // nothing needs to happen here
                    }
                    exercises = it.exerciseList
                    initRv()
                }
            }
        }
    }

    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvExercises.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvExercises.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
        binding.rvExercises.adapter = exerciseAdapter
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Keep this empty.
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Keep this empty.
        }

        //after the user inputted text into the search bar this method is being called.
        override fun afterTextChanged(p0: Editable?) {
            filter(p0.toString())
        }
    }


    //here a new arrayList is created where the new notes will be stored, that fit in the filter.
    private fun filter(inputText: String) {
        val filteredList = arrayListOf<Exercise>()

        for (exercise in exercises) {
            if (exercise.title.lowercase().contains(inputText.lowercase())) {
                filteredList.add(exercise)
            }
        }
        exerciseAdapter.filterList(filteredList)
    }

}