package nl.medify.doctoruser.feature_diagnosis.presentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentAddDiagnosisBinding
import nl.medify.doctoruser.feature_diagnosis.presentation.vm.DiagnosisViewModel
import nl.medify.doctoruser.feature_patient_list.presentation.vm.PatientListViewModel
import nl.medify.utilities.data_classes.Patient
import java.time.LocalDate

@AndroidEntryPoint
class AddDiagnosisFragment : Fragment() {

    private var _binding: FragmentAddDiagnosisBinding? = null
    private val binding get() = _binding!!

    private val diagnosisViewModel: DiagnosisViewModel by activityViewModels()
    private val patientListViewModel: PatientListViewModel by activityViewModels()
    private lateinit var selectedPatient: Patient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDiagnosisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedPatient = patientListViewModel.selectedPatient!!
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        // set page title
        binding.header.tvTitle.text = getString(R.string.add_diagnosis_text)

        // set navigation(s)
        binding.header.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSubmit.setOnClickListener {
            creatingDiagnosis()
        }
    }

    private fun creatingDiagnosis() {
        val diagnosisTitle = binding.etDiagnosisTitle.text.toString()
        val dateDiagnosed = LocalDate.now()
        val diagnosisCause = binding.etDiagnosisCause.text.toString()
        val diagnosisTreatment = binding.etDiagnosisTreatment.text.toString()
        val diagnosisAdvice = binding.etAdvice.text.toString()

        // Checking if inputs are not empty
        if (diagnosisTitle.isBlank() || diagnosisCause.isBlank() || diagnosisTreatment.isBlank() || diagnosisAdvice.isBlank() /*|| diagnosisType.isBlank() */) {
            Toast.makeText(
                activity, "One or more inputs are not filled in", Toast.LENGTH_LONG
            ).show()
        } else {
            openConfirmationDialog(
                diagnosisTitle,
                dateDiagnosed,
                diagnosisCause,
                diagnosisTreatment,
                diagnosisAdvice
            )
        }
    }

    private fun openConfirmationDialog(
        diagnosisTitle: String,
        dateDiagnosed: LocalDate,
        diagnosisCause: String,
        diagnosisTreatment: String,
        diagnosisAdvice: String
    ) {
        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(nl.medify.utilities.R.layout.alert_dialog_two_options)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val dialogTitle = dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvTitleOfDialog)
        dialogTitle.text = getString(R.string.add_diagnosis_text)
        val dialogMessage =
            dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvMessageOfDialog)
        dialogMessage.text = getString(R.string.are_you_sure_add_diagnosis)
        val dialogRemoveBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.noThanks)
        dialogRemoveBtn.text = getString(R.string.cancel)
        val dialogOkSureBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.okSure)
        dialogOkSureBtn.text = getString(R.string.confirm)

        dialogRemoveBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialogOkSureBtn.setOnClickListener {
            dialog.dismiss()

            // TODO: pass real diagnosisType (add dropdown menu to select diagnosis type)
            diagnosisViewModel.postCreateDiagnosis(
                diagnosisTitle,
                "Heart",
                dateDiagnosed,
                diagnosisCause,
                diagnosisTreatment,
                diagnosisAdvice,
                selectedPatient.medicalRecordId
            )
            findNavController().navigate(R.id.action_addDiagnosisFragment_to_diagnosisListFragment)
        }

        dialog.show()
    }

}