package nl.medify.patientuser.feature_calendar.presentation.make_appointment

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.medify.patientuser.R
import nl.medify.patientuser.databinding.FragmentConfirmAppointmentBinding
import nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.AppointmentViewModel
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel
import java.time.format.DateTimeFormatter
import java.util.*
import nl.medify.utilities.R as utilR

@AndroidEntryPoint
class ConfirmAppointmentFragment : Fragment() {

    private var _binding: FragmentConfirmAppointmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val appointmentViewModel: AppointmentViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()

    private var permissionChecker = checkAndActOnPermission(
        toDoIfGranted = { insertCalendarEntry() },
        toDoIfNotGranted = { askUserAgainForCalendarPermissions() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.container.tvTitle.text = getString(R.string.step_count, 4)
        binding.progressContainer.btnNext.text = getString(R.string.confirm)

        val selectedDoctorOrCareProvider = appointmentViewModel.selectedDoctorOrCareProvider

        selectedDoctorOrCareProvider?.addressDTO?.let { appointmentViewModel.getGeoCodes(it.city, it.street, it.zip) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appointmentViewModel.geoCodes.collect {
                    it.geoCode?.let { it1 -> setMapLocation(it1) }
                }
            }
        }

        binding.appointmentContainer.tvInfoDoctor.text = selectedDoctorOrCareProvider?.let { "${it.firstName} ${it.lastName}" }
        binding.appointmentContainer.tvInfoLocation.text = selectedDoctorOrCareProvider?.addressDTO?.let { "${it.street}, ${it.zip}, ${it.city}" }
        binding.appointmentContainer.tvInfoTimeDate.text = appointmentViewModel.chosenDateTime?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, 'at' HH:mm"))
        binding.appointmentContainer.tvInfoReasonForVisit.text = appointmentViewModel.reasonOfVisit

//        permissionChecker.launch(Manifest.permission.WRITE_CALENDAR)
        binding.progressContainer.run {
            btnNext.setOnClickListener {
                loginViewModel.loggedInPatient?.let { appointmentViewModel.postCreateAppointment(it.id) }
                findNavController().navigate(R.id.action_confirmAppointmentFragment_to_completedMakingAppointmentFragment)
            }
            btnPrevious.setOnClickListener { findNavController().popBackStack() }
        }
        binding.container.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setMapLocation(adressInLatLng: LatLng) {
        with(binding.mapView) {
            // Initialize the mapview
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync {
                MapsInitializer.initialize(requireContext())
                with(it) {
                    moveCamera(CameraUpdateFactory.newLatLngZoom(adressInLatLng, 16.0F))
                    addMarker(MarkerOptions().position(adressInLatLng))
                }
            }
        }
    }

    fun checkAndActOnPermission(
        toDoIfGranted: () -> Unit,
        toDoIfNotGranted: () -> Unit,
    ): ActivityResultLauncher<String> {
        return registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                toDoIfGranted()
            } else {
                toDoIfNotGranted()
            }
        }
    }

    fun askUserAgainForCalendarPermissions() {

        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(utilR.layout.alert_dialog_two_options)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val dialogRemoveBtn = dialog.findViewById<TextView>(utilR.id.noThanks)
        val dialogOkSureBtn = dialog.findViewById<TextView>(utilR.id.okSure)

        dialogRemoveBtn.setOnClickListener { dialog.dismiss() }
        dialogOkSureBtn.setOnClickListener {
            dialog.cancel()
//            permissionChecker.launch(Manifest.permission.WRITE_CALENDAR)
        }

        dialog.show()
    }

    fun insertCalendarEntry(
        appointmentTitle: String = "test entry",
        appointmentDescription: String = "test booking",
        appointmentLocation: String = "Amsterdam",
        appointmentDay: Int = 4,
        appointmentHourOfDayStart: Int = 20,
        appointmentMinuteStart: Int = 30,
        appointmentHourOfDayEnd: Int = 21,
        appointmentMinuteEnd: Int = 30,
        appointmentMonth: Int = 11, /* months are 0 to 11 in Calendar class, wtf ??????? */
        appointmentYear: Int = 2022 /* goin be fixed soon diss */
    ) {
        val values = ContentValues()
        val calendarID: Long = 3
        val cr = requireActivity().contentResolver
        val beginTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()

        beginTime.set(
            appointmentYear,
            appointmentMonth,
            appointmentDay,
            appointmentHourOfDayStart,
            appointmentMinuteStart
        )
        endTime.set(
            appointmentYear,
            appointmentMonth,
            appointmentDay,
            appointmentHourOfDayEnd,
            appointmentMinuteEnd
        )

        values.put(CalendarContract.Events.DTSTART, beginTime.timeInMillis)
        values.put(CalendarContract.Events.DTEND, endTime.timeInMillis)
        values.put(CalendarContract.Events.TITLE, appointmentTitle)
        values.put(CalendarContract.Events.DESCRIPTION, appointmentDescription)
        values.put(CalendarContract.Events.CALENDAR_ID, calendarID)
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Paris")
        values.put(CalendarContract.Events.EVENT_LOCATION, appointmentLocation)
        val uri: Uri? = cr.insert(CalendarContract.Events.CONTENT_URI, values)

        Toast.makeText(requireContext(), "Calendar Event added", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}