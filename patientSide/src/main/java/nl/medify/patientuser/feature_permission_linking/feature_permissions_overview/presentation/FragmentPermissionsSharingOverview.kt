package nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.presentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.medify.patientuser.R
import nl.medify.patientuser.databinding.FragmentPermissionsSharingOverviewBinding
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.model.sharedDataItem
import nl.medify.patientuser.feature_permission_linking.vm.PermissionTransferViewModel


class FragmentPermissionsSharingOverview : Fragment() {
    private var _binding: FragmentPermissionsSharingOverviewBinding? = null
    private val binding get() = _binding!!

    private var dataItemList = arrayListOf<sharedDataItem>()

    private lateinit var careProvidersOverviewListAdapter: CareProvidersOverviewListAdapter
    private lateinit var linkingOverviewDataAdapter: LinkingOverviewDataAdapter
    private val permissionTransferViewModel: PermissionTransferViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionsSharingOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun initView() {
        dataItemList.clear()
        binding.container.tvTitle.setText(nl.medify.utilities.R.string.step_four_four)

        binding.btnGivePermission.setOnClickListener {
            confirmData()
        }

        binding.container.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvDate.text = permissionTransferViewModel.selectedDate

        //Launch recycler views
        initRvCareProvider()
        initRvItemData()

    }

    /**
     * Get all the selected care-providers and show them to the user
     */
    private fun initRvCareProvider() {
        // Initialize the recycler view with a linear layout manager, adapter
        careProvidersOverviewListAdapter =
            CareProvidersOverviewListAdapter(permissionTransferViewModel.listOfCareProviders)
        binding.rvCareGivers.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvCareGivers.adapter = careProvidersOverviewListAdapter
        binding.rvCareGivers.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    /**
     * Get all the selected data and place it in the generic list to show
     */
    private fun gatherDataToShow() {
        if (permissionTransferViewModel.listOfMedication.isNotEmpty()) {
            permissionTransferViewModel.listOfMedication.forEach {
                dataItemList.add(
                    sharedDataItem(
                        getString(R.string.medication),
                        it.title,
                        it.description
                    )
                )
            }
        }
        if (permissionTransferViewModel.listOfDiagnosis.isNotEmpty()) {
            permissionTransferViewModel.listOfDiagnosis.forEach {
                dataItemList.add(sharedDataItem(getString(R.string.diagnosis), it.title, it.cause))
            }
        }
        if (permissionTransferViewModel.listOfExercises.isNotEmpty()) {
            permissionTransferViewModel.listOfExercises.forEach {
                dataItemList.add(
                    sharedDataItem(
                        getString(R.string.exercises),
                        it.title,
                        it.description
                    )
                )
            }
        }
        if (permissionTransferViewModel.listOfNotes.isNotEmpty()) {
            permissionTransferViewModel.listOfNotes.forEach {
                dataItemList.add(sharedDataItem(getString(R.string.note), it.title, it.description))
            }
        }
    }

    private fun initRvItemData() {
        //Get the data to show the user
        gatherDataToShow()

        linkingOverviewDataAdapter =
            LinkingOverviewDataAdapter(dataItemList)
        binding.rvSharedItems.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvSharedItems.adapter = linkingOverviewDataAdapter
        binding.rvSharedItems.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    /**
     * Make and handle confirmation dialog
     */
    private fun confirmData() {
        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(nl.medify.utilities.R.layout.alert_dialog_two_options)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val dialogTitle = dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvTitleOfDialog)
        dialogTitle.text = getString(R.string.share_data)
        val dialogMessage = dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvMessageOfDialog)
        dialogMessage.text = getString(R.string.are_you_sure_share_data)
        val dialogRemoveBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.noThanks)
        val dialogOkSureBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.okSure)
        dialogOkSureBtn.text = getString(R.string.share)

        dialogRemoveBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialogOkSureBtn.setOnClickListener {
            dialog.dismiss()
            // Confirmed that the user wants to share data
            sendData()
        }

        dialog.show()
    }

    /**
     * Grant permissions to the selected data to the careproviders
     */
    private fun sendData() {
        // Go thru the list of care-providers
        permissionTransferViewModel.listOfCareProviders.forEach {
            if (permissionTransferViewModel.listOfMedication.isNotEmpty()) {
                sendMedicationLinks(it.id)
            }
            if (permissionTransferViewModel.listOfDiagnosis.isNotEmpty()) {
                sendDiagnosisLinks(it.id)
            }
            if (permissionTransferViewModel.listOfExercises.isNotEmpty()) {
                sendExerciseLinks(it.id)
            }
            if (permissionTransferViewModel.listOfNotes.isNotEmpty()) {
                sendNoteLinks(it.id)
            }
        }

        findNavController().navigate(R.id.action_fragmentPermissionsSharingOverview_to_homeFragment)
    }

    private fun sendMedicationLinks(careProviderId: String) {
        permissionTransferViewModel.listOfMedication.forEach {
            permissionTransferViewModel.postMedicationPermissionLink(
                it.id, careProviderId,
                permissionTransferViewModel.selectedDate!!
            )
        }
    }

    private fun sendDiagnosisLinks(careProviderId: String) {
        permissionTransferViewModel.listOfDiagnosis.forEach {
            permissionTransferViewModel.postDiagnosisPermissionLink(
                it.id, careProviderId,
                permissionTransferViewModel.selectedDate!!
            )
        }
    }

    private fun sendExerciseLinks(careProviderId: String) {
        permissionTransferViewModel.listOfExercises.forEach {
            permissionTransferViewModel.postExercisePermissionLink(
                it.id, careProviderId,
                permissionTransferViewModel.selectedDate!!
            )
        }
    }

    private fun sendNoteLinks(careProviderId: String) {
        permissionTransferViewModel.listOfNotes.forEach {
            permissionTransferViewModel.postNotePermissionLink(
                it.id, careProviderId,
                permissionTransferViewModel.selectedDate!!
            )
        }
    }


}