package com.example.noteapp.feature_note.presentation.edit_add_note

data class NoteTextFieldState(
    var text: String = "",
    val hint: String = "",
    var isHintVisible: Boolean = true
)