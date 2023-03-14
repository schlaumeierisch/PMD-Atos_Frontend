package nl.medify.doctoruser.feature_notes.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.utilities.data_classes.Note
import nl.medify.utilities.databinding.ItemNoteBinding
import nl.medify.utilities.R as utilR

class NoteListAdapter(private var notes: List<Note>) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemNoteBinding.bind(itemView)

        fun databind(note: Note) {
            binding.tvNoteTitle.text = note.title
            binding.tvNoteDate.text = note.date.toString()
            binding.tvNoteDescription.text = note.description
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(utilR.layout.item_note, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return notes.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(notes[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Note> = arrayListOf()) {
        notes = filteredList
        notifyDataSetChanged()
    }
}
