package nl.medify.doctoruser.feature_home.presentation

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentHomeBinding
import nl.medify.doctoruser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.doctoruser.feature_calendar.presentation.calendar_screen.CalendarAdapter
import nl.medify.doctoruser.feature_calendar.presentation.calendar_screen.vm.CalendarViewModel
import nl.medify.doctoruser.feature_login.domain.model.UserRole
import nl.medify.doctoruser.feature_login.presentation.LoginActivity
import nl.medify.doctoruser.feature_login.presentation.vm.LoginViewModel
import nl.medify.utilities.util.hide

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarAdapter: CalendarAdapter
    private val calendarViewModel: CalendarViewModel by activityViewModels()

    private lateinit var firebaseAuth: FirebaseAuth
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var loggedInUser: DoctorOrCareProvider
    private lateinit var loggedInUserRole: UserRole

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        loggedInUser = loginViewModel.loggedInUser!!
        loggedInUserRole = loginViewModel.loggedInUserRole!!

        calendarViewModel.getAllAppointmentsOfDoctor("dom-id-gp-001")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calendarViewModel.appointmentList.collect { appointmentListState ->
                    if (appointmentListState.appointmentList.isNotEmpty()) {
                        calendarAdapter =
                            CalendarAdapter(appointmentListState.appointmentList.subList(0, 2)) {
                                calendarViewModel.saveSelectedAppointment(it)
                                findNavController().navigate(R.id.action_homeFragment_to_seeOrCancelAppointment)
                            }
                        initRvViews()
                    }
                }
            }
        }

        initViews()
    }

    private fun initViews() {
        // welcome text
        binding.tvWelcome.text = getString(
            R.string.welcome_txt,
            loggedInUser.firstName,
            loggedInUser.lastName
        )

        // set navigation(s)
        binding.btnPatientList.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_patientlistsFragment)
        }

        // logout button
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.btnAllAppointments.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }
    }

    private fun initRvViews() {
        binding.rvIncomingAppointments.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvIncomingAppointments.adapter = calendarAdapter
        binding.activitySection.root.hide()
        binding.rvIncomingAppointments.hide(status = View.VISIBLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}