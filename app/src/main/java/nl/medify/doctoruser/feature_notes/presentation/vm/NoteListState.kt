package nl.medify.doctoruser.feature_notes.presentation.vm

import nl.medify.utilities.data_classes.Note

data class NoteListState(
    val isLoading: Boolean = false,
    val noteList: List<Note> = emptyList(),
    val error: String = ""
)