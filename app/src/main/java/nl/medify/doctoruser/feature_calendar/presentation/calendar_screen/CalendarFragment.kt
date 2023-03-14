package nl.medify.doctoruser.feature_calendar.presentation.calendar_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentCalendarBinding
import nl.medify.doctoruser.feature_calendar.presentation.calendar_screen.vm.CalendarViewModel
import nl.medify.doctoruser.feature_login.presentation.vm.LoginViewModel
import nl.medify.utilities.util.hide

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val calendarViewModel: CalendarViewModel by activityViewModels()
    private lateinit var calendarAdapter: CalendarAdapter

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.header.tvTitle.setText(R.string.your_appointments)
        binding.header.btnBack.hide()

//        binding.btnBookAppointment.setOnClickListener {
//            findNavController().navigate(R.id.action_calendarFragment_to_pickDoctorFragment)
//        }

        loginViewModel.loggedInUser?.let {
            calendarViewModel.getAllAppointmentsOfDoctor(it.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calendarViewModel.appointmentList.collect { appointmentListState ->
                    calendarAdapter = CalendarAdapter(appointmentListState.appointmentList.sortedBy { it.dateAndTime }) {
                        calendarViewModel.saveSelectedAppointment(it)
                        findNavController().navigate(R.id.action_calendarFragment_to_seeOrCancelAppointment)
                    }
                    initViews()
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            loginViewModel.loggedInUser?.let {
                calendarViewModel.getAllAppointmentsOfDoctor(it.id)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initViews() {
        binding.rvAppointments.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvAppointments.adapter = calendarAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}