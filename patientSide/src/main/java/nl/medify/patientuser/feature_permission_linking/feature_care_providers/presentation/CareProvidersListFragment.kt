package nl.medify.patientuser.feature_permission_linking.feature_care_providers.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import nl.medify.patientuser.databinding.FragmentCareprovidersListBinding
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel
import nl.medify.patientuser.feature_permission_linking.feature_care_providers.presentation.vm.CareProviderViewModel
import nl.medify.patientuser.feature_permission_linking.vm.PermissionTransferViewModel

@AndroidEntryPoint
class CareProvidersListFragment : Fragment() {
    private var _binding: FragmentCareprovidersListBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by activityViewModels()
    private var selectedCareProviders = arrayListOf<DoctorOrCareProvider>()

    private val transferDataViewModel: PermissionTransferViewModel by activityViewModels()
    private var careProviders = listOf<DoctorOrCareProvider>()
    private lateinit var careProvidersListAdapter: CareProvidersListAdapter

    private val careProviderViewModel: CareProviderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCareprovidersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onStart() {
        super.onStart()
        // user enters the careProviders page
        transferDataViewModel.visitedCareProviderPage = true
        transferDataViewModel.deleteListOfCareProviders(transferDataViewModel.listOfCareProviders)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // user leaves the careProviders page
        transferDataViewModel.visitedCareProviderPage = false
    }

    private fun initViews() {
        informingUserAboutDataReset()

        // Add listener for search/filter bar
        binding.etSearchCareProvider.addTextChangedListener(textWatcher)

        binding.container.tvTitle.setText(R.string.step_two_four)

        binding.container.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNextPage.setOnClickListener() {
            if(checkIfAnyCareProviderIsChecked()) {
                nextPageCheck()
            }else{
                Toast.makeText(activity, nl.medify.utilities.R.string.no_care_provider_selected_warning, Toast.LENGTH_SHORT).show()
            }
        }

        careProviderViewModel.getAllCareProviders(loginViewModel.loggedInPatient!!.id)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                careProviderViewModel.careProviderList.collect {
                    careProvidersListAdapter =
                        CareProvidersListAdapter(it.careProviderList) { selectedCareProvider, isChecked ->
                            careProviderClicked(selectedCareProvider, isChecked)
                        }
                    careProviders = it.careProviderList
                    initRv()
                }
            }
        }
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

    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvCareProviderList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvCareProviderList.adapter = careProvidersListAdapter
        binding.rvCareProviderList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun nextPageCheck() {
        //save careProvider list in transfer view model
        transferDataViewModel.saveListOfCareProviders(selectedCareProviders)

        findNavController().navigate(R.id.action_careProvidersListFragment_to_fragmentDateConfirmation)
    }

    private fun careProviderClicked(careProvider: DoctorOrCareProvider, isChecked: Boolean) {
        if (isChecked) {
            selectedCareProviders.add(careProvider)
        } else {
            selectedCareProviders.remove(careProvider)
        }
    }

    private fun checkIfAnyCareProviderIsChecked() : Boolean {
        return selectedCareProviders.isNotEmpty()
    }

    private fun informingUserAboutDataReset(){
        if(transferDataViewModel.visitedDateConfirmationPage){
            binding.txtErrorPrevention.visibility = View.VISIBLE
        }else{
            binding.txtErrorPrevention.visibility = View.GONE
        }
    }

    // Here a new arrayList is created where the new notes will be stored in, that fit in the filter.
    private fun filter(inputText: String) {
        val filteredList = arrayListOf<DoctorOrCareProvider>()

        for (careProvider in careProviders) {
            if (careProvider.firstName.lowercase().contains(inputText.lowercase()) ||
                careProvider.lastName.lowercase().contains(inputText) ||
                careProvider.specialism!!.lowercase().contains(inputText)
            ) {
                filteredList.add(careProvider)
            }

            // This new arraylist will be swapped with the current list in the recycleView
            careProvidersListAdapter.filterList(filteredList)
        }
    }
}