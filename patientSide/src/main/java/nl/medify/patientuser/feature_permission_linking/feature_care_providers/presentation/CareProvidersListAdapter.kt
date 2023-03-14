package nl.medify.patientuser.feature_permission_linking.feature_care_providers.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.utilities.R
import nl.medify.utilities.databinding.ItemCareproviderSelectionBinding
import java.util.*

class CareProvidersListAdapter(
    var careProviders: List<DoctorOrCareProvider>,
    private val clickListener: (DoctorOrCareProvider, Boolean) -> Unit
) : RecyclerView.Adapter<CareProvidersListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemCareproviderSelectionBinding.bind(itemView)

        fun databind(
            careProvider: DoctorOrCareProvider
        ) {
            binding.tvCareProviderName.text = careProvider.firstName + " " + careProvider.lastName
            binding.tvSpecialismValue.text = careProvider.specialism.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            binding.cbSelect.setOnClickListener { clickListener(careProviders[adapterPosition], binding.cbSelect.isChecked)}


        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_careprovider_selection, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return careProviders.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(careProviders[position], )
    }

    fun filterList(filteredList: ArrayList<DoctorOrCareProvider> = arrayListOf<DoctorOrCareProvider>()) {
        careProviders = filteredList
        notifyDataSetChanged()
    }
}