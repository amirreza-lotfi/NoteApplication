package com.example.noteapp.feature_note.presentation.edit_add_note.event

sealed class UiEvent {
    data class ShowSnackbar(val message:String): UiEvent()
    object SaveNote: UiEvent()
}