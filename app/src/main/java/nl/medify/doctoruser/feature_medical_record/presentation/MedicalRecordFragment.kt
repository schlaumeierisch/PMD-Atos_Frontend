package nl.medify.doctoruser.feature_medical_record.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentMedicalRecordBinding

class MedicalRecordFragment : Fragment() {

    private var _binding: FragmentMedicalRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMedicalRecordBinding.inflate(inflater, container, false)
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
        binding.container.tvTitle.setText(R.string.medical_record)

        // set navigation(s)
        binding.container.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_medicalRecordFragment_to_patientlistsFragment)
        }
        binding.btnNotes.setOnClickListener {
            findNavController().navigate(R.id.action_medicalRecordFragment_to_noteListFragment)
        }
        binding.btnMedication.setOnClickListener {
            findNavController().navigate(R.id.action_medicalRecordFragment_to_medicationListFragment)
        }
        binding.btnExercises.setOnClickListener {
            findNavController().navigate(R.id.action_medicalRecordFragment_to_exerciseListFragment)
        }
        binding.btnDiagnosis.setOnClickListener {
            findNavController().navigate(R.id.action_medicalRecordFragment_to_diagnosisListFragment)
        }
    }
}