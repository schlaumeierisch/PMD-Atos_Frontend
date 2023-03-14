package nl.medify.patientuser.feature_home.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.medify.patientuser.R
import nl.medify.patientuser.databinding.FragmentHomeBinding
import nl.medify.patientuser.feature_home.presentation.vm.HomeViewModel
import nl.medify.patientuser.feature_login.presentation.LoginActivity
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel
import nl.medify.patientuser.feature_medication.presentation.MedicationListAdapter
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.util.hide
import kotlin.math.roundToInt


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var medicationAdapter: MedicationListAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var firebaseAuth: FirebaseAuth
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var loggedInPatient: Patient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        loggedInPatient = loginViewModel.loggedInPatient!!

        homeViewModel.getMedicationOfPatient(loggedInPatient.medicalRecordId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.medicationList.collect { medicationListState ->
                    if(medicationListState.medicationList.isNotEmpty()) {
                        medicationAdapter = MedicationListAdapter(medicationListState.medicationList.subList(0, 2), true, null)
                        initRvViews()
                    }
                }
            }
        }

        initViews()
    }

    private fun initViews() {
        // welcome text
        binding.tvWelcome.text = getString(R.string.welcome_txt, loggedInPatient.firstName, loggedInPatient.lastName)

        binding.btnAllTreatments.updateLayoutParams<ConstraintLayout.LayoutParams> {
            setMargins(32, 16, 0, 0)
        }

        // disable back button
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}

        // set navigation(s)
        binding.btnMedicalRecord.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_medicalRecordFragment)
        }
        binding.btnPermissions.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_permissionsMedicalRecordFragment)
        }

        binding.btnAllTreatments.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_medicationListFragment)
        }
    }

    private fun initRvViews() {
        binding.rvIncomingMedication.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvIncomingMedication.adapter = medicationAdapter
        binding.activitySection.root.hide()
        binding.rvIncomingMedication.hide(status = View.VISIBLE)

        // logout button
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}