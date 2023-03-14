package nl.medify.doctoruser.feature_diagnosis.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.doctoruser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.utilities.databinding.ItemDiagnosisBinding
import nl.medify.utilities.R as utilR

class DiagnosisListAdapter(private var diagnoses: List<Diagnosis>) : RecyclerView.Adapter<DiagnosisListAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemDiagnosisBinding.bind(itemView)

        fun databind(diagnosis: Diagnosis) {

            binding.tvDiagnosisTitle.text = diagnosis.title
            binding.tvDiagnosisDate.text = diagnosis.dateDiagnosed
            binding.tvDiagnosisCause.text = makeBold(itemView.context.getString(nl.medify.utilities.R.string.cause_value, diagnosis.cause))
            binding.tvDiagnosisTreatment.text = makeBold(itemView.context.getString(nl.medify.utilities.R.string.treatment_value, diagnosis.treatment))
            binding.tvDiagnosisAdvice.text = makeBold(itemView.context.getString(nl.medify.utilities.R.string.advice_value, diagnosis.advice))

        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(utilR.layout.item_diagnosis, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return diagnoses.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(diagnoses[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Diagnosis> = arrayListOf()) {
        diagnoses = filteredList
        notifyDataSetChanged()
    }

    /**
     * Make part of the string bold
     */
    private fun makeBold(text: String): SpannableStringBuilder{
        val endIndex = text.indexOf(":") + 1;

        val spannableString = SpannableStringBuilder(text)
        spannableString.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        return spannableString
    }

}