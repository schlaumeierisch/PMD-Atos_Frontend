package nl.medify.doctoruser.feature_patient_list.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.databinding.ItemPatientBinding
import nl.medify.utilities.R as utilR

class PatientListAdapter(private var patients: List<Patient>, private val clickListener: (Patient) -> Unit) : RecyclerView.Adapter<PatientListAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemPatientBinding.bind(itemView)

        fun databind(patient: Patient, clickListener: (Patient) -> Unit) {
            binding.tvPatientName.text = patient.lastName + " " + patient.firstName.first() + "."
            binding.tvDatePatientList.text = patient.birthDate
            binding.root.setOnClickListener { clickListener(patient) }
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(utilR.layout.item_patient, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return patients.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(patients[position], clickListener)
    }

    fun filterList(filteredList: ArrayList<Patient> = arrayListOf()) {
        patients = filteredList
        notifyDataSetChanged()
    }
}