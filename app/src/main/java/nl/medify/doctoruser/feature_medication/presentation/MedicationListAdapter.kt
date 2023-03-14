package nl.medify.doctoruser.feature_medication.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.doctoruser.R
import nl.medify.doctoruser.feature_medication.domain.model.Medication
import nl.medify.utilities.databinding.ItemMedicationBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt
import nl.medify.utilities.R as utilR

class MedicationListAdapter(private var medications: List<Medication>) :
    RecyclerView.Adapter<MedicationListAdapter.ViewHolder>() {
    private var progress: Int = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemMedicationBinding.bind(itemView)

        fun databind(medication: Medication) {
            binding.tvMedicationName.text = medication.title
            binding.tvMedicationInformation.text = medication.description
            binding.tvProgressDays.text =
                calculateDays(itemView.context, medication.endDate, medication.startDate)
            binding.progressBar.progress = progress
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(utilR.layout.item_medication, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return medications.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(medications[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Medication> = arrayListOf()) {
        medications = filteredList
        notifyDataSetChanged()
    }

    /**
     * Calculate the number of days by comparing the starting and end date
     */
    private fun calculateDays(context: Context, endDate: String, startDate: String): String {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // get start & end date
        val start = LocalDate.parse(startDate, dateFormatter)
        val end = LocalDate.parse(endDate, dateFormatter)

        if (start == end) {
            return context.getString(R.string.completed_txt)
        }

        // get total days (period) between start & end date
        val totalDays = ChronoUnit.DAYS.between(start, end)

        // get days that are already finished
        val daysTaken = ChronoUnit.DAYS.between(start, LocalDate.now())

        // calculate the progress bar
        progress = (daysTaken.toDouble() / totalDays * 100).roundToInt()

        return if (progress >= 100) {
            context.getString(R.string.completed_txt)
        } else {
            context.getString(
                R.string.progress_days,
                daysTaken.toString(),
                totalDays.toString()
            )
        }
    }

}