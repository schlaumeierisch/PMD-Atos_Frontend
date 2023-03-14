package nl.medify.patientuser.feature_exercises.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.patientuser.R
import nl.medify.patientuser.feature_exercises.domain.model.Exercise
import nl.medify.utilities.databinding.ItemExerciseBinding
import nl.medify.utilities.databinding.ItemPermissionExerciseBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt
import nl.medify.utilities.R as utilR

class ExerciseListAdapter(
    var exercises: List<Exercise>,
    val state: Boolean,
    private val clickListener: (Exercise, Boolean) -> Unit
) : RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {
    private var progress: Int = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun databind(exercise: Exercise) {
            if (state) {
                val binding = ItemExerciseBinding.bind(itemView)
                binding.tvExerciseTitle.text = exercise.title
                binding.tvExerciseAdviser.text = exercise.description
                binding.tvProgressDays.text =
                    calculateDays(itemView.context, exercise.endDate, exercise.startDate)
                binding.progressBar.progress = progress
            } else {
                val bindingPermission = ItemPermissionExerciseBinding.bind(itemView)
                bindingPermission.tvExercisesTitle.text = exercise.title
                bindingPermission.tvDescription.text = exercise.description
                bindingPermission.tvExerciseProgress.text =
                    calculateDays(itemView.context, exercise.endDate, exercise.startDate)

                bindingPermission.rbSelectExercises.setOnClickListener {
                    clickListener(
                        exercises[adapterPosition],
                        bindingPermission.rbSelectExercises.isChecked
                    )
                }
            }
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (state) {
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(utilR.layout.item_exercise, parent, false)
            )
        } else {
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(utilR.layout.item_permission_exercise, parent, false)
            )
        }
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return exercises.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(exercises[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Exercise> = arrayListOf()) {
        exercises = filteredList
        this.notifyDataSetChanged()
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