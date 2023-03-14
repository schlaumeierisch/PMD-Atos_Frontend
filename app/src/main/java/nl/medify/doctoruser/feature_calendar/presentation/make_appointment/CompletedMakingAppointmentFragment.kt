package nl.medify.doctoruser.feature_calendar.presentation.make_appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentCompletedMakingAppointmentBinding
import nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.AppointmentViewModel
import nl.medify.utilities.util.hide
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class CompletedMakingAppointmentFragment : Fragment() {

    private var _binding: FragmentCompletedMakingAppointmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    // TODO: Manually (destroy if possible?) this activityViewModel when completing appointment because otherwise data will remain in viewModel for whole lifetime of app!
    // TODO: Same problem for other features too. Because for the whole lifetime of the 'activity' the data will be stored in the created viewmodel.
    // TODO: Look at [by navGraphViewModels()] maybe? Looks good for this process. https://stackoverflow.com/a/61929651/14554493
    // TODO: Look at this whole thread actually hehe -> https://stackoverflow.com/questions/53653157/manually-clearing-an-android-viewmodel
    private val appointmentViewModel: AppointmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompletedMakingAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.container.btnBack.hide()

        val selectedDoctorOrCareProvider = appointmentViewModel.selectedPatient

        binding.appointmentContainer.tvInfoDoctor.text = selectedDoctorOrCareProvider?.let { "${it.firstName} ${it.lastName}" }
        binding.appointmentContainer.tvInfoLocation.text = selectedDoctorOrCareProvider?.addressDTO?.let { "${it.street}, ${it.zip}, ${it.city}" }
        binding.appointmentContainer.tvInfoTimeDate.text = appointmentViewModel.chosenDateTime?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, 'at' HH:mm"))
        binding.appointmentContainer.tvInfoReasonForVisit.text = appointmentViewModel.reasonOfVisit

        binding.btnFinish.setOnClickListener {
            findNavController().navigate(R.id.action_completedMakingAppointmentFragment_to_calendarFragment)
        }
    }
}