package com.example.noteapp.feature_note.presentation.edit_add_note

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.use_case.UseCases
import com.example.noteapp.feature_note.presentation.edit_add_note.event.AddEditNoteEvent
import com.example.noteapp.feature_note.presentation.edit_add_note.event.UiEvent
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditAddNoteViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel()
{
    companion object{
        private var editNote = Note(id=-1)
    }

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    val titleState: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content"
    ))
    val contentState: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val colorState: State<Int> = _noteColor

    private val currentNoteId = MutableLiveData(-1L)


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init{
        val id = editNote.id
        if(id!=-1L){
            _noteTitle.value = titleState.value.copy(
                text = editNote.title
            )
            _noteContent.value = contentState.value.copy(
                text = editNote.content
            )
            _noteColor.value = editNote.color
            currentNoteId.value = editNote.id
        }
    }




    fun onEvent(event: AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.TitleEntered ->{
                _noteTitle.value = titleState.value.copy(
                    text = event.title
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus ->{
                _noteTitle.value = titleState.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            titleState.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ContentEntered ->{
                _noteContent.value = _noteContent.value.copy(
                    text = event.content
                )
            }
            is AddEditNoteEvent.ChangeContentFocus->{
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor ->{
                _noteColor.value = event.color
            }

            is AddEditNoteEvent.SaveNote->{
                viewModelScope.launch {
                    try {
                        useCases.addNoteUseCase(
                            Note(
                                title = titleState.value.text,
                                content = contentState.value.text,
                                time = System.currentTimeMillis(),
                                color = colorState.value,
                                id = if (currentNoteId.value == -1L) System.currentTimeMillis() else currentNoteId.value!!
                            )
                        )
                        cleanScreenStates()
                        _eventFlow.emit(UiEvent.SaveNote)
                    }catch (exception:Exception){
                       _eventFlow.emit(UiEvent.ShowSnackbar(
                            message = exception.message ?: "Can not save note"
                        ))
                    }
                }
            }
        }
    }

    fun initializeNoteScreen(note: Note) {
        editNote = note
        Log.i(""," ")
    }
    private fun cleanScreenStates(){
        editNote = Note(-1)
    }
}