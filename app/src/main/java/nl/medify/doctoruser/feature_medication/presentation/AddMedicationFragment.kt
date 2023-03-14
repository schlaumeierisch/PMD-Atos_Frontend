package nl.medify.doctoruser.feature_medication.presentation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentAddMedicationBinding
import nl.medify.doctoruser.feature_medication.presentation.vm.MedicationViewModel
import nl.medify.doctoruser.feature_patient_list.presentation.vm.PatientListViewModel
import nl.medify.utilities.data_classes.Patient
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class AddMedicationFragment : Fragment() {

    private var _binding: FragmentAddMedicationBinding? = null
    private val binding get() = _binding!!

    private val medicationViewModel: MedicationViewModel by activityViewModels()
    private val patientListViewModel: PatientListViewModel by activityViewModels()
    private lateinit var selectedPatient: Patient

    private var datePickerDialog: DatePickerDialog? = null
    private var dateButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMedicationBinding.inflate(inflater, container, false)

        initDatePicker()
        dateButton = binding.datePickerButton
        dateButton?.text = LocalDate.now().toString()

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
        binding.header.tvTitle.text = getString(R.string.add_medication)

        // set navigation(s)
        binding.header.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.datePickerButton.setOnClickListener {
            openDatePicker()
        }
        binding.btnSubmit.setOnClickListener {
            creatingExercise()
        }
    }

    private fun creatingExercise() {
        val medicationTitle = binding.etMedicationTitle.text.toString()
        val medicationDescription = binding.etMedicationInput.text.toString()
        val startDate = LocalDate.now()
        val endDate = LocalDate.parse(dateButton?.text)

        // Checking if inputs are not empty
        if (medicationTitle.isBlank() || medicationDescription.isBlank()) {
            Toast.makeText(
                activity,
                "One or more inputs are not filled in", Toast.LENGTH_LONG
            ).show()
        } else {
            openConfirmationDialog(
                medicationTitle,
                medicationDescription,
                startDate,
                endDate
            )
        }
    }

    private fun openConfirmationDialog(
        medicationTitle: String,
        medicationDescription: String,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(nl.medify.utilities.R.layout.alert_dialog_two_options)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val dialogTitle = dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvTitleOfDialog)
        dialogTitle.text = getString(R.string.add_medication)
        val dialogMessage =
            dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvMessageOfDialog)
        dialogMessage.text = getString(R.string.are_you_sure_add_medication)
        val dialogRemoveBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.noThanks)
        dialogRemoveBtn.text = getString(R.string.cancel)
        val dialogOkSureBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.okSure)
        dialogOkSureBtn.text = getString(R.string.confirm)

        dialogRemoveBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialogOkSureBtn.setOnClickListener {
            dialog.dismiss()

            medicationViewModel.postCreateMedication(
                medicationTitle,
                medicationDescription,
                startDate,
                endDate,
                selectedPatient.medicalRecordId
            )
            findNavController().navigate(R.id.action_addMedicationFragment_to_medicationListFragment)
        }

        dialog.show()
    }

    @Suppress("DEPRECATION")
    private fun initDatePicker() {
        val dateSetListener =
            OnDateSetListener { _, year, month, day ->
                var month = month
                month += 1
                val date = LocalDate.of(year, month, day)
                this.dateButton?.text = date.toString()
            }
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog =
            DatePickerDialog(binding.root.context, style, dateSetListener, year, month, day)
        datePickerDialog?.datePicker?.minDate = System.currentTimeMillis() - 1000
    }

    private fun openDatePicker() {
        datePickerDialog?.show()
    }
}