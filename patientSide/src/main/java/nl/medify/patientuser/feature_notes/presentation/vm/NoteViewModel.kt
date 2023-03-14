package nl.medify.patientuser.feature_notes.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.medify.patientuser.feature_notes.domain.use_case.NoteUseCases
import nl.medify.utilities.util.Resource
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _noteList = MutableStateFlow(NoteListState())
    var noteList: StateFlow<NoteListState> = _noteList

    fun getAllNotes(id: String) = viewModelScope.launch(Dispatchers.IO) {
        noteUseCases.getAllNotesUseCase.invoke(id).collect {
            when(it) {
                is Resource.Success -> {
                    _noteList.value = NoteListState(noteList = it.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _noteList.value = NoteListState(isLoading = true)
                }
                is Resource.Error -> {
                    _noteList.value = NoteListState(error = it.message ?: "Unexpected error occurred.")
                }
                else -> {
                    Log.e(this.javaClass.name, "'it.data' is null")
                }
            }
        }
    }

    fun postCreateNote(title: String, description: String, medicalRecordID: String) = viewModelScope.launch(Dispatchers.IO) {
        noteUseCases.postCreateNoteUseCases.invoke(title, description, medicalRecordID)
    }
}