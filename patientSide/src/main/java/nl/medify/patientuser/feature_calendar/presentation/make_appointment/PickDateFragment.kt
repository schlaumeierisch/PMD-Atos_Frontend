package nl.medify.patientuser.feature_calendar.presentation.make_appointment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.medify.patientuser.R
import nl.medify.patientuser.databinding.FragmentPickDateBinding
import nl.medify.patientuser.feature_calendar.presentation.make_appointment.vm.AppointmentViewModel
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel
import nl.medify.utilities.util.toCalendarDays
import java.time.LocalDate
import java.time.Month

@AndroidEntryPoint
class PickDateFragment : Fragment() {

    private var _binding: FragmentPickDateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var appointmentViewModel: AppointmentViewModel
    private lateinit var dateStart: LocalDate

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

        loginViewModel.loggedInPatient?.let { appointmentViewModel.getNotAvailableDaysInThisMonth(dateStart.year, dateStart.monthValue, it.id) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appointmentViewModel.notAvailableDaysList.collect {
                    initViews(it.notAvailableDaysList)
                }
            }
        }

        binding.progressContainer.run {
            btnNext.setOnClickListener { findNavController().navigate(R.id.action_pickDateFragment_to_pickTimeFragment) }
            btnPrevious.setOnClickListener { findNavController().popBackStack() }
        }
        binding.container.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun initViews(notAvailableDaysInThisMonth: List<LocalDate>) {
        binding.datepicker.run {
            addDecorator(AllDaysDisabledDecorator())
            addDecorator(NotAvailableDaysDecorator(notAvailableDaysInThisMonth.toCalendarDays()))
            var temp = appointmentViewModel.findFirstAvailableDay(dateStart.month, notAvailableDaysInThisMonth)
            while (temp == null) {
                var checkForDecember: Int
                var nextYearIfNextMonthJanuary = dateStart.year
                if (dateStart.monthValue == 12) {
                    checkForDecember = 1
                    nextYearIfNextMonthJanuary += 1
                } else {
                    checkForDecember = dateStart.monthValue + 1
                }
                appointmentViewModel.selectedDoctorOrCareProvider?.let {
                    appointmentViewModel.getNotAvailableDaysInThisMonth(nextYearIfNextMonthJanuary, checkForDecember, it.id)
                }
                temp = appointmentViewModel.findFirstAvailableDay(Month.of(checkForDecember), notAvailableDaysInThisMonth)
                if (temp != null) {
                    break
                }
            }
            if (temp != null) {
                state().edit()
                    .setMinimumDate(CalendarDay.from(temp.year, temp.month, temp.day))
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit()
            }
            setOnDateChangedListener { widget, date, selected ->
                appointmentViewModel.saveDate(LocalDate.of(date.year, date.month, date.day))
            }
        }
    }

    class AllDaysDisabledDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return true //decorate all days in calendar
        }

        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(true) //disable all days
        }
    }

    class NotAvailableDaysDecorator internal constructor(
        dates: Collection<CalendarDay>
    ) : DayViewDecorator {
        private val dates: HashSet<CalendarDay>

        init {
            this.dates = HashSet(dates)
        }

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return !dates.contains(day) //decorate only available days
        }

        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(false) ///important to enable day
        }
    }

    // So basically, app was crashing before this, because the fragment tried to run viewModel code
    // before it was attached to the Activity. And the fragment always needs to be attached to a activity
    // before it can actually do something. That's why in this case we're only initializing the viewModel when
    // the fragment is attached to the activity.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        appointmentViewModel = activityViewModels<AppointmentViewModel>().value
        dateStart = appointmentViewModel.dateToday
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}