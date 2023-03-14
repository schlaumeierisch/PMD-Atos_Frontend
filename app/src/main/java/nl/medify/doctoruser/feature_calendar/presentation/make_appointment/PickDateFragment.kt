package nl.medify.doctoruser.feature_calendar.presentation.make_appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import dagger.hilt.android.AndroidEntryPoint
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentPickDateBinding
import nl.medify.doctoruser.feature_calendar.presentation.make_appointment.vm.AppointmentViewModel
import java.time.LocalDate


@AndroidEntryPoint
class PickDateFragment : Fragment() {

    private var _binding: FragmentPickDateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val appointmentViewModel: AppointmentViewModel by activityViewModels()
    private val dateToday = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.container.tvTitle.text = getString(R.string.step_count, 2)

        binding.datepicker.run {
            state().edit()
                .setMinimumDate(CalendarDay.from(dateToday.year, dateToday.monthValue, dateToday.dayOfMonth))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
            selectedDate = CalendarDay.today()
            setOnDateChangedListener { widget, date, selected ->
                appointmentViewModel.saveDate(LocalDate.of(date.year, date.month, date.day))
            }
        }

        binding.progressContainer.run {
            btnNext.setOnClickListener { findNavController().navigate(R.id.action_pickDateFragment_to_pickTimeFragment) }
            btnPrevious.setOnClickListener { findNavController().popBackStack() }
        }
        binding.container.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}