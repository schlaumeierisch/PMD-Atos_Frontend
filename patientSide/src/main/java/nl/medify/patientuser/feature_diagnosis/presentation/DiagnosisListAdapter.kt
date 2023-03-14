package nl.medify.patientuser.feature_diagnosis.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.patientuser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.utilities.databinding.ItemDiagnosisBinding
import nl.medify.utilities.databinding.ItemPermissionDiagnosisBinding
import nl.medify.utilities.R as utilR

class DiagnosisListAdapter(private var diagnoses: List<Diagnosis>, val state: Boolean, private val clickListener: (Diagnosis, Boolean) -> Unit) : RecyclerView.Adapter<DiagnosisListAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun databind(diagnosis: Diagnosis) {
            if(state) {
                val binding = ItemDiagnosisBinding.bind(itemView)
                binding.tvDiagnosisTitle.text = diagnosis.title
                binding.tvDiagnosisDate.text = diagnosis.dateDiagnosed
                binding.tvDiagnosisCause.text = diagnosis.cause
                binding.tvDiagnosisTreatment.text = diagnosis.treatment
                binding.tvDiagnosisAdvice.text = diagnosis.advice

            }else{
                val bindingPermission = ItemPermissionDiagnosisBinding.bind(itemView)
                bindingPermission.tvDiagnosisTitle.text = diagnosis.title
                bindingPermission.tvDiagnosisDate.text = diagnosis.dateDiagnosed
                bindingPermission.tvDiagnosisCause.text = diagnosis.cause
                bindingPermission.tvDiagnosisTreatment.text = diagnosis.treatment
                bindingPermission.tvDiagnosisAdvice.text = diagnosis.advice

                bindingPermission.rbSelectDiagnosis.setOnClickListener { clickListener(diagnoses[adapterPosition],
                    bindingPermission.rbSelectDiagnosis.isChecked) }
            }
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(state) {
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(utilR.layout.item_diagnosis, parent, false)
            )
        }else{
            ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(utilR.layout.item_permission_diagnosis, parent, false)
            )
        }
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

}