package nl.medify.patientuser.feature_medication.presentation

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
import nl.medify.patientuser.databinding.FragmentMedicationListBinding
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel
import nl.medify.patientuser.feature_medication.domain.model.Medication
import nl.medify.patientuser.feature_medication.presentation.vm.MedicationViewModel

@AndroidEntryPoint
class MedicationListFragment : Fragment() {

    private var _binding: FragmentMedicationListBinding? = null
    private val binding get() = _binding!!

    private lateinit var medicationListAdapter: MedicationListAdapter
    private val medicationViewModel: MedicationViewModel by viewModels()
    private var medications = listOf<Medication>()

    private var state: Boolean = true

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicationListBinding.inflate(inflater, container, false)
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
        binding.header.tvTitle.setText(R.string.medication)

        // set navigation(s)
        binding.header.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_medicationListFragment_to_medicalRecordFragment)
        }

        // add listener for search/filter bar
        binding.etFilterMedicationList.addTextChangedListener(textWatcher)

        // Get the data to fill the recyclerview
        medicationViewModel.getMedicationOfPatient(loginViewModel.loggedInPatient!!.medicalRecordId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                medicationViewModel.medicationList.collect {
                    medicationListAdapter =
                        MedicationListAdapter(it.medicationList, state) { _, _ ->
                            // nothing needs to happen here.
                        }
                    medications = it.medicationList
                    initRv()
                }
            }
        }
    }

    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvMedicationList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvMedicationList.adapter = medicationListAdapter
        binding.rvMedicationList.addItemDecoration(
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
        val filteredList = arrayListOf<Medication>()

        for (medicine in medications) {
            if (medicine.title.lowercase().contains(inputText.lowercase())) {
                filteredList.add(medicine)
            }
        }
        medicationListAdapter.filterList(filteredList)
    }
}