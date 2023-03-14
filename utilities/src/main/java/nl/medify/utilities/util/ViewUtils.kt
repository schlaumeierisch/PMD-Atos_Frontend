package nl.medify.utilities.util

import android.view.View
import android.widget.EditText
import androidx.core.view.doOnLayout
import com.google.android.material.textfield.TextInputLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import nl.medify.utilities.data_classes.PlaceholderTimeSlot
import nl.medify.utilities.data_classes.TimeSlot
import java.time.LocalDate
import java.time.LocalTime

fun View.hide(status: Int = View.GONE) {
    visibility = status
}

fun TextInputLayout.setHintStyle(id: Int) {
    doOnLayout {
        setHintTextAppearance(id)
    }
}

fun EditText.getInputOfEditable(): String {
    return this.text.toString()
}

fun String.toOnlyFirstLetterCapitalized(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

fun List<LocalDate>.toCalendarDays(): Collection<CalendarDay> {
    return this.map { CalendarDay.from(it.year, it.monthValue, it.dayOfMonth) }
}

fun List<PlaceholderTimeSlot>.toTimeSlots(): List<TimeSlot> {
    return this.map { TimeSlot(LocalTime.parse(it.startTime), LocalTime.parse(it.endTime)) }
}