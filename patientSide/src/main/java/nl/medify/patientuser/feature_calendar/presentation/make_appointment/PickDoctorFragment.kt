package nl.medify.patientuser.feature_calendar.presentation.make_appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.medify.patientuser.R
import nl.medify.patientuser.databinding.FragmentPickDoctorBinding
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.AppointmentViewModel
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel
import nl.medify.utilities.util.getInputOfEditable
import nl.medify.utilities.util.setHintStyle
import nl.medify.utilities.util.toOnlyFirstLetterCapitalized
import nl.medify.utilities.R as utilR

@AndroidEntryPoint
class PickDoctorFragment : Fragment() {

    private var _binding: FragmentPickDoctorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val appointmentViewModel: AppointmentViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val items: ArrayList<String> = ArrayList()
    private var hasChosenSomething: Boolean = false
    private val providers: ArrayList<DoctorOrCareProvider> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.container.tvTitle.text = getString(R.string.step_count, 1)
        binding.tilDropdownMenu.setHintStyle(utilR.style.dropdown_menu_item)

        loginViewModel.loggedInPatient?.let { appointmentViewModel.getAllDoctorsAndCareProvidersOfPatient(it.id) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appointmentViewModel.allDoctorAndCareProvidersOfPatient.collect {
                    it.allDoctorsAndCareProvidersOfPatient?.forEach { doctorOrCareProvider ->
                        providers.add(doctorOrCareProvider)
                        doctorOrCareProvider.specialism?.let { specialism ->
                            items.add("${doctorOrCareProvider.firstName} ${doctorOrCareProvider.lastName} (${specialism.toOnlyFirstLetterCapitalized()})")
                        } ?: items.add("${doctorOrCareProvider.firstName} ${doctorOrCareProvider.lastName} (${getString(R.string.GP)})")
                    }
                }
            }
        }

        binding.dropdownField.setOnItemClickListener { adapterView, view, position, id ->
            hasChosenSomething = true
            appointmentViewModel.saveSelectedDoctorOrCareProviderID(providers[position])
        }

        binding.container.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {
            if(binding.etReasonForVisit.getInputOfEditable().isNotBlank() && hasChosenSomething) {
                appointmentViewModel.saveReasonOfVisit(binding.etReasonForVisit.getInputOfEditable())
                findNavController().navigate(R.id.action_pickDoctorFragment_to_pickDateFragment)
            } else {
                Toast.makeText(requireContext(), getString(R.string.fill_in_all_fields), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        providers.clear()
        items.clear()
        val adapter = ArrayAdapter(requireContext(), utilR.layout.item_picker_dropdown_menu, items)
        binding.dropdownField.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}