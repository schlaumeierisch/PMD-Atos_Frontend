package nl.medify.doctoruser.feature_notes.presentation

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
import nl.medify.doctoruser.databinding.FragmentAddNoteBinding
import nl.medify.doctoruser.feature_login.presentation.vm.LoginViewModel
import nl.medify.doctoruser.feature_notes.presentation.vm.NoteViewModel
import nl.medify.doctoruser.R
import nl.medify.doctoruser.feature_patient_list.presentation.vm.PatientListViewModel
import nl.medify.utilities.data_classes.Patient

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel: NoteViewModel by activityViewModels()
    private val patientListViewModel: PatientListViewModel by activityViewModels()
    private lateinit var selectedPatient: Patient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
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
        binding.container.tvTitle.text = getString(R.string.add_note_txt)

        // set navigation(s)
        binding.container.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSubmit.setOnClickListener {
            creatingNote()
        }
    }

    private fun creatingNote() {
        val noteTitle = binding.etNoteTitle.text.toString()
        val noteDescription = binding.etNoteInput.text.toString()

        // Checking if inputs are not empty
        if (noteTitle.isBlank() || noteDescription.isBlank()) {
            Toast.makeText(
                activity, "One or more inputs are not filled in", Toast.LENGTH_LONG
            ).show()
        } else {
            openConfirmationDialog(
                noteTitle,
                noteDescription
            )
        }
    }

    private fun openConfirmationDialog(
        noteTitle: String,
        noteDescription: String
    ) {

        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(nl.medify.utilities.R.layout.alert_dialog_two_options)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val dialogTitle = dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvTitleOfDialog)
        dialogTitle.text = getString(R.string.add_note_txt)
        val dialogMessage =
            dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvMessageOfDialog)
        dialogMessage.text = getString(R.string.are_you_sure_add_note)
        val dialogRemoveBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.noThanks)
        dialogRemoveBtn.text = getString(R.string.cancel)
        val dialogOkSureBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.okSure)
        dialogOkSureBtn.text = getString(R.string.confirm)

        dialogRemoveBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialogOkSureBtn.setOnClickListener {
            dialog.dismiss()
            noteViewModel.postCreateNote(
                noteTitle,
                noteDescription,
                selectedPatient.medicalRecordId
            )
            findNavController().navigate(R.id.action_addNoteFragment_to_noteListFragment)
        }

        dialog.show()
    }
}