package nl.medify.patientuser.feature_calendar.presentation.calendar_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.patientuser.feature_calendar.domain.model.ShowAppointment
import nl.medify.utilities.R
import nl.medify.utilities.databinding.AppointmentCardViewBinding
import java.time.format.DateTimeFormatter

class CalendarAdapter(private val appointments: List<ShowAppointment>, private val clickListener: (ShowAppointment) -> Unit) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = AppointmentCardViewBinding.bind(itemView)

        fun bind(showAppointment: ShowAppointment) {
            binding.tvAppointmentDoctor.text = showAppointment.name
            binding.tvAppointmentDate.text = showAppointment.dateAndTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, 'at' HH:mm"))
            binding.tvAppointmentLocation.text = showAppointment.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.appointment_card_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(appointments[position])
        holder.itemView.setOnClickListener { clickListener(appointments[position]) }
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

}

