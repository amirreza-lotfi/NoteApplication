package com.example.noteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.use_case.UseCases
import com.example.noteapp.feature_note.domain.util.NoteOrder
import com.example.noteapp.feature_note.domain.util.OrderType
import com.example.noteapp.feature_note.presentation.edit_add_note.EditAddNoteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesUseCase: UseCases
): ViewModel() {

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }


    private val _noteState = mutableStateOf(NoteState())
    val state: State<NoteState> = _noteState

    private var recentlyNote: Note?= null
    private var getNotesJob: Job? = null

    fun onEvent(event: NoteEvent){
        when(event){
            is NoteEvent.OrderEvent -> {
                if(event.noteOrder::class == state.value.noteOrder::class &&
                    event.noteOrder.orderType::class == state.value.noteOrder.orderType::class){
                    return
                }
                getNotes(event.noteOrder)
            }
            is NoteEvent.DeleteNoteEvent -> {
                viewModelScope.launch {
                    notesUseCase.deleteNoteUseCase(event.note)
                    recentlyNote = event.note
                }
            }
            is NoteEvent.ToggleSectionEvent -> {
                _noteState.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NoteEvent.RestoreNoteEvent -> {
                viewModelScope.launch{
                    recentlyNote?.let { notesUseCase.addNoteUseCase(it) }
                    recentlyNote = null
                }
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder){
        getNotesJob?.cancel()
        getNotesJob = notesUseCase.getNotesUseCase(noteOrder)
            .onEach { notes->
                _noteState.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

    fun onNavigateToEditAddNoteScreen(
        viewModelOfAddEditNoteScreen: EditAddNoteViewModel,
        note: Note
    ){
        viewModelOfAddEditNoteScreen.initializeNoteScreen(note)
    }
}