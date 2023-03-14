package nl.medify.patientuser.feature_permission_linking.feature_date_confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import nl.medify.patientuser.R
import nl.medify.patientuser.databinding.FragmentDateConfirmationBinding
import nl.medify.patientuser.feature_permission_linking.vm.PermissionTransferViewModel


class FragmentDateConfirmation : Fragment() {
    private var _binding: FragmentDateConfirmationBinding? = null
    private val binding get() = _binding!!
    private val datePickerMonthCorrection = 1
    private val dateUnlimited = "3022-01-01"
    private val permissionTransferViewModel: PermissionTransferViewModel by activityViewModels()
    private var selectedDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        permissionTransferViewModel.visitedDateConfirmationPage = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        permissionTransferViewModel.visitedDateConfirmationPage = false
    }

    private fun initView() {
        binding.container.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNextPage.setOnClickListener {
            nextPageCheck()
        }

        binding.container.tvTitle.text = getString(R.string.step_three_four)

        binding.cvDateSelector.setMinDate(System.currentTimeMillis() - 1000)
        binding.cvDateSelector.setOnDateChangeListener { view, year, month, dayOfMonth ->
            changeDataFormat(year, month, dayOfMonth)
        }

        binding.cbSelect.setOnClickListener {
            toggleWarning()
        }
    }

    private fun changeDataFormat(year: Int, month: Int, day: Int) {
        //check the days for the correct format
        val selectedMonth = checkFormat(month + datePickerMonthCorrection)
        val selectedDay = checkFormat(day)
        selectedDate = "$year-$selectedMonth-$selectedDay"
    }

    /**
     * Check and change the format where necessary to localDate
     */
    private fun checkFormat(numberToCheck: Int): String {
        return if (numberToCheck >= 10) {
            "$numberToCheck"
        } else {
            "0$numberToCheck"
        }
    }

    /**
     * Toggle warning text for unlimited duration
     */
    private fun toggleWarning() {
        if (binding.cbSelect.isChecked) {
            binding.tvWarning.visibility = View.VISIBLE
        } else {
            binding.tvWarning.visibility = View.GONE
        }
    }


    /**
     * Check if the user has selected a date option or give a warning
     */
    private fun nextPageCheck() {
        if (binding.cbSelect.isChecked) {
            permissionTransferViewModel.saveSelectedDate(dateUnlimited)
            nextPage()
        } else if (selectedDate != null) {
            permissionTransferViewModel.saveSelectedDate(selectedDate!!)
            nextPage()

        } else {
            Toast.makeText(activity, nl.medify.utilities.R.string.warning_toast, Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun nextPage() {
        findNavController().navigate(R.id.action_fragmentDateConfirmation_to_fragmentPermissionsSharingOverview)
    }

}