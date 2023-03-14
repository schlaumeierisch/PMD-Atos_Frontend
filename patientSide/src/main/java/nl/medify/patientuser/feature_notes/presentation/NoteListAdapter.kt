import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.medify.utilities.data_classes.Note
import nl.medify.utilities.databinding.ItemNoteBinding
import nl.medify.utilities.databinding.ItemPermissionNoteBinding
import nl.medify.utilities.R as utilR

class NoteListAdapter(var notes: List<Note>, val state: Boolean, private val clickListener: (Note, Boolean) -> Unit) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun databind(note: Note) {
            if(state){
                var binding = ItemNoteBinding.bind(itemView)
                binding.tvNoteTitle.text = note.title
                binding.tvNoteDate.text = note.date
                binding.tvNoteDescription.text = note.description

            } else{
                var bindingPermission = ItemPermissionNoteBinding.bind(itemView)
                bindingPermission.tvNoteTitle.text = note.title
                bindingPermission.tvNoteDate.text = note.date
                bindingPermission.tvNoteDescription.text = note.description

                bindingPermission.rbSelectNote.setOnClickListener { clickListener(notes[adapterPosition],
                    bindingPermission.rbSelectNote.isSelected) }
            }
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(state) {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(utilR.layout.item_note, parent, false)
            )
        } else{
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(utilR.layout.item_permission_note, parent, false)
            )
        }
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

    fun filterList(filteredList: ArrayList<Note> = arrayListOf<Note>()) {
        notes = filteredList
        notifyDataSetChanged()
    }
}
