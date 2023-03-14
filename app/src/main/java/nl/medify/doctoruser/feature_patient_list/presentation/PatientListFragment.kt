package nl.medify.doctoruser.feature_patient_list.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentPatientListBinding
import nl.medify.doctoruser.feature_login.domain.model.UserRole
import nl.medify.doctoruser.feature_login.presentation.vm.LoginViewModel
import nl.medify.doctoruser.feature_patient_list.presentation.vm.PatientListViewModel
import nl.medify.utilities.data_classes.Patient

@AndroidEntryPoint
class PatientListFragment : Fragment() {

    private var _binding: FragmentPatientListBinding? = null
    private val binding get() = _binding!!

    private var patients = listOf<Patient>()
    private lateinit var patientListAdapter: PatientListAdapter

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val patientListViewModel: PatientListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientListBinding.inflate(inflater, container, false)
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
        binding.header.tvTitle.setText(R.string.patient_list)

        // set navigation(s)
        binding.header.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_patientlistsFragment_to_homeFragment)
        }

        // add listener for search/filter bar
        binding.etFilterPatientList.addTextChangedListener(textWatcher)

        // Get the data to fill the recyclerview
        if (loginViewModel.loggedInUserRole == UserRole.GENERAL_PRACTITIONER) {
            patientListViewModel.getPatientsOfGeneralPractitionerById(loginViewModel.loggedInUser!!.id)
        } else {
            patientListViewModel.getPatientsOfCareProviderById(loginViewModel.loggedInUser!!.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                patientListViewModel.patientList.collect {
                    patientListAdapter = PatientListAdapter(it.patientList) { patient: Patient ->
                        patientItemClicked(patient)
                    }
                    patients = it.patientList
                    initRv()
                }
            }
        }
    }

    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvPatientList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvPatientList.adapter = patientListAdapter
        binding.rvPatientList.addItemDecoration(
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
        val filteredList = arrayListOf<Patient>()

        for (patient in patients) {
            if (patient.lastName.lowercase().contains(inputText.lowercase())) {
                filteredList.add(patient)
            } else if (patient.birthDate.lowercase().contains(inputText.lowercase())) {
                filteredList.add(patient)
            }
        }
        patientListAdapter.filterList(filteredList)
    }

    private fun patientItemClicked(patient: Patient) {
        patientListViewModel.selectedPatient = patient

        findNavController().navigate(R.id.action_patientlistsFragment_to_patientProfileFragment)
    }

}