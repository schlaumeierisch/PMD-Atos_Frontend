package nl.medify.patientuser.feature_calendar.presentation.see_or_cancel_appointment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.medify.patientuser.R
import nl.medify.patientuser.databinding.FragmentSeeOrCancelAppointmentBinding
import nl.medify.patientuser.feature_calendar.domain.model.ShowAppointment
import nl.medify.patientuser.feature_calendar.presentation.calendar_screen.vm.CalendarViewModel
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SeeOrCancelAppointment : Fragment() {

    private var _binding: FragmentSeeOrCancelAppointmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val calendarViewModel: CalendarViewModel by activityViewModels()
    private lateinit var selectedAppointment: ShowAppointment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeOrCancelAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.header.tvTitle.setText(R.string.cancel_appointment)
        binding.header.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        selectedAppointment = calendarViewModel.selectedAppointment!!

        selectedAppointment.let {
            val temp = it.address.split(',')
            Log.d("geooo", "$temp")
            calendarViewModel.getGeoCodes(temp[0], temp[1], temp[2])
        }

        binding.appointmentContainer.tvInfoDoctor.text = selectedAppointment.name
        binding.appointmentContainer.tvInfoLocation.text = selectedAppointment.address
        binding.appointmentContainer.tvInfoTimeDate.text = selectedAppointment.dateAndTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, 'at' HH:mm"))
        binding.appointmentContainer.tvInfoReasonForVisit.text = selectedAppointment.reasonForVisit

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calendarViewModel.geoCodes.collect {
                    it.geoCode?.let { it1 -> binding.mapView.setMapLocation(it1) }
                }
            }
        }

        binding.btnCancelAppointment.setOnClickListener {
            askUserAgainForCancellingAppointment()
        }
    }

    private fun askUserAgainForCancellingAppointment() {

        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(nl.medify.utilities.R.layout.alert_dialog_two_options)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val dialogTitle = dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvTitleOfDialog)
        dialogTitle.text = getString(R.string.cancel_appointment)
        val dialogMessage = dialog.findViewById<TextView>(nl.medify.utilities.R.id.tvMessageOfDialog)
        dialogMessage.text = getString(R.string.are_you_sure_cancel_appointment)
        val dialogRemoveBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.noThanks)
        val dialogOkSureBtn = dialog.findViewById<TextView>(nl.medify.utilities.R.id.okSure)
        dialogOkSureBtn.text = getString(R.string.cancel)

        dialogRemoveBtn.setOnClickListener { dialog.dismiss() }
        dialogOkSureBtn.setOnClickListener {
            dialog.dismiss()
            calendarViewModel.cancelAppointment(selectedAppointment.appointmentID)
            findNavController().popBackStack()
        }

        dialog.show()
    }

    private fun MapView.setMapLocation(adressInLatLng: LatLng) {
        with(this) {
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
}