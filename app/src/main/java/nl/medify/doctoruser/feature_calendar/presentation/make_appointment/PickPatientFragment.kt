package nl.medify.doctoruser.feature_calendar.presentation.make_appointment

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
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentPickPatientBinding
import nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.AppointmentViewModel
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.util.getInputOfEditable
import nl.medify.utilities.util.setHintStyle
import nl.medify.utilities.R as utilR

@AndroidEntryPoint
class PickPatientFragment : Fragment() {

    private var _binding: FragmentPickPatientBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val appointmentViewModel: AppointmentViewModel by activityViewModels()
    private val items: ArrayList<String> = ArrayList()
    private var hasChosenSomething: Boolean = false
    private val patients: ArrayList<Patient> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.container.tvTitle.text = getString(R.string.step_count, 1)
        binding.tilDropdownMenu.setHintStyle(utilR.style.dropdown_menu_item)

        appointmentViewModel.getAllPatientsOfDoctor("dom-id-gp-001")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appointmentViewModel.allPatients.collect {
                    it.allPatientsOfDoctorOrCareProvider?.forEach { patient ->
                        patients.add(patient)
                        items.add("${patient.firstName} ${patient.lastName}")
                    }
                }
            }
        }

        binding.dropdownField.setOnItemClickListener { adapterView, view, position, id ->
            hasChosenSomething = true
            appointmentViewModel.saveSelectedPatient(patients[position])
        }

        binding.btnNext.setOnClickListener {
            if(binding.etReasonForVisit.getInputOfEditable().isNotBlank() && hasChosenSomething) {
                appointmentViewModel.saveReasonOfVisit(binding.etReasonForVisit.getInputOfEditable())
                findNavController().navigate(R.id.action_pickDoctorFragment_to_pickDateFragment)
            } else {
                Toast.makeText(requireContext(), getString(R.string.fill_in_all_fields), Toast.LENGTH_LONG).show()
            }
        }
        binding.container.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onResume() {
        super.onResume()
        patients.clear()
        items.clear()
        val adapter = ArrayAdapter(requireContext(), utilR.layout.item_picker_dropdown_menu, items)
        binding.dropdownField.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}