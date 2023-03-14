package nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.patientuser.feature_permission_linking.feature_permissions_overview.domain.model.sharedDataItem
import nl.medify.utilities.R
import nl.medify.utilities.databinding.ItemSharedDataBinding

class LinkingOverviewDataAdapter (
    var selectedItems: List<sharedDataItem>
) : RecyclerView.Adapter<LinkingOverviewDataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemSharedDataBinding.bind(itemView)

        fun databind(
            sharedItem: sharedDataItem
        ) {
            binding.tvDataCategory.text = sharedItem.category
            binding.tvDataTitle.text = sharedItem.title
            binding.tvDataDescription.text = sharedItem.description

        }
    }
    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkingOverviewDataAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shared_data, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return selectedItems.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: LinkingOverviewDataAdapter.ViewHolder, position: Int) {
        holder.databind(selectedItems[position], )
    }
}