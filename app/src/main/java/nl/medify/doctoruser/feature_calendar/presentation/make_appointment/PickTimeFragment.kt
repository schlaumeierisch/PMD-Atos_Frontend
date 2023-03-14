package nl.medify.doctoruser.feature_calendar.presentation.make_appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentPickTimeBinding
import nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.AppointmentViewModel
import nl.medify.utilities.util.setHintStyle
import java.time.LocalTime
import nl.medify.utilities.R as utilR

@AndroidEntryPoint
class PickTimeFragment : Fragment() {

    private var _binding: FragmentPickTimeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val appointmentViewModel: AppointmentViewModel by activityViewModels()
    private var hasChosenSomething: Boolean = false
    private val items: ArrayList<LocalTime> = arrayListOf(LocalTime.of(15, 30))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.container.tvTitle.text = getString(R.string.step_count, 3)
        binding.tilDropdownMenuTime.setHintStyle(utilR.style.dropdown_menu_item)

        binding.dropdownFieldTime.setOnItemClickListener { adapterView, view, position, id ->
            hasChosenSomething = true
            appointmentViewModel.saveTime(items[position])
        }

        binding.progressContainer.run {
            btnNext.setOnClickListener {
                if(hasChosenSomething) { findNavController().navigate(R.id.action_pickTimeFragment_to_confirmAppointmentFragment) }
                else { Toast.makeText(requireContext(), getString(R.string.select_a_time_please), Toast.LENGTH_LONG).show() }
            }
            btnPrevious.setOnClickListener { findNavController().popBackStack() }
        }
        binding.container.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onResume() {
        super.onResume()
        val adapter = ArrayAdapter(requireContext(), utilR.layout.item_picker_dropdown_menu, items)
        binding.dropdownFieldTime.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}