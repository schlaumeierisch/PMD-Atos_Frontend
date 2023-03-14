package nl.medify.doctoruser.feature_diagnosis.presentation

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
import nl.medify.doctoruser.databinding.FragmentDiagnosisListBinding
import nl.medify.doctoruser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.doctoruser.feature_diagnosis.presentation.vm.DiagnosisViewModel
import nl.medify.doctoruser.feature_login.domain.model.UserRole
import nl.medify.doctoruser.feature_login.presentation.vm.LoginViewModel
import nl.medify.doctoruser.feature_patient_list.presentation.vm.PatientListViewModel
import nl.medify.utilities.data_classes.Patient

@AndroidEntryPoint
class DiagnosisListFragment : Fragment() {

    private var _binding: FragmentDiagnosisListBinding? = null
    private val binding get() = _binding!!

    private lateinit var diagnosisListAdapter: DiagnosisListAdapter
    private val diagnosisViewModel: DiagnosisViewModel by viewModels()
    private var diagnoses = listOf<Diagnosis>()

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val patientListViewModel: PatientListViewModel by activityViewModels()
    private lateinit var selectedPatient: Patient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiagnosisListBinding.inflate(inflater, container, false)
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
        binding.header.tvTitle.setText(R.string.diagnosis)

        // set navigation(s)
        binding.header.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_diagnosisListFragment_to_medicalRecordFragment)
        }
        binding.btnAddDiagnosis.setOnClickListener {
            findNavController().navigate(R.id.action_diagnosisListFragment_to_addDiagnosisFragment)
        }

        // enable/disable the add diagnosis button
        if (loginViewModel.loggedInUserRole == UserRole.GENERAL_PRACTITIONER) {
            binding.btnAddDiagnosis.visibility = View.VISIBLE
        } else {
            binding.btnAddDiagnosis.visibility = View.INVISIBLE
        }

        // add listener for search/filter bar
        binding.etFilterMedicationList.addTextChangedListener(textWatcher)

        // Get the data to fill the recyclerview
        if (loginViewModel.loggedInUserRole == UserRole.GENERAL_PRACTITIONER) {
            diagnosisViewModel.getAllDiagnoses(selectedPatient.medicalRecordId)
        } else {
            diagnosisViewModel.getDiagnosisOfMedicalRecord(
                selectedPatient.medicalRecordId,
                loginViewModel.loggedInUser!!.id
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                diagnosisViewModel.diagnoseList.collect {
                    diagnosisListAdapter = DiagnosisListAdapter(it.diagnosisList)
                    diagnoses = it.diagnosisList
                    initRv()
                }
            }
        }
    }

    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvDiagnoses.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvDiagnoses.adapter = diagnosisListAdapter
        binding.rvDiagnoses.addItemDecoration(
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
        val filteredList = arrayListOf<Diagnosis>()

        for (diagnosis in diagnoses) {
            if (diagnosis.title.lowercase().contains(inputText.lowercase())) {
                filteredList.add(diagnosis)
            }
        }
        diagnosisListAdapter.filterList(filteredList)
    }

}