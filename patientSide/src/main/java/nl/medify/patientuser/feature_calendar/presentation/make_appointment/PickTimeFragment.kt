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
import nl.medify.patientuser.databinding.FragmentPickTimeBinding
import nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.AppointmentViewModel
import nl.medify.utilities.data_classes.TimeSlot
import nl.medify.utilities.util.setHintStyle
import java.time.temporal.ChronoUnit

@AndroidEntryPoint
class PickTimeFragment : Fragment() {

    private var _binding: FragmentPickTimeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val appointmentViewModel: AppointmentViewModel by activityViewModels()
    private var hasChosenSomething: Boolean = false
    private val items: ArrayList<String> = ArrayList()
    private val timeslots: ArrayList<TimeSlot> = ArrayList()

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
        binding.tilDropdownMenuTime.setHintStyle(nl.medify.utilities.R.style.dropdown_menu_item)

        binding.dropdownFieldTime.setOnItemClickListener { adapterView, view, position, id ->
            hasChosenSomething = true
            appointmentViewModel.saveTime(timeslots[position].startTime) // backend only needs start time of timeslot for now.
        }

        val particularDayChosen = appointmentViewModel.chosenDate
        if (particularDayChosen != null) {
            appointmentViewModel.selectedDoctorOrCareProvider?.let {
                appointmentViewModel.getAvailableTimeSlotsOfParticularDay(
                    particularDayChosen.year,
                    particularDayChosen.monthValue,
                    particularDayChosen.dayOfMonth,
                    it.id
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appointmentViewModel.availableTimeSlots.collect {
                    it.availableTimeSlots.forEach { timeslot ->
                        timeslots.add(timeslot)
                        items.add("${timeslot.startTime.truncatedTo(ChronoUnit.MINUTES)} - ${timeslot.endTime.truncatedTo(ChronoUnit.MINUTES)}")
                    }
                }
            }
        }

        binding.progressContainer.run {
            btnNext.setOnClickListener {
                if (hasChosenSomething) { findNavController().navigate(R.id.action_pickTimeFragment_to_confirmAppointmentFragment) }
                else { Toast.makeText(requireContext(), getString(R.string.select_a_time_please), Toast.LENGTH_LONG).show() }
            }
            btnPrevious.setOnClickListener { findNavController().popBackStack() }
        }
        binding.container.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onResume() {
        super.onResume()
        val adapter = ArrayAdapter(
            requireContext(),
            nl.medify.utilities.R.layout.item_picker_dropdown_menu,
            items
        )
        binding.dropdownFieldTime.setAdapter(adapter)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}