package com.example.noteapp.feature_note.presentation.edit_add_note.event

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent{
    data class TitleEntered(val title:String):AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState):AddEditNoteEvent()
    data class ContentEntered(val content:String):AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState):AddEditNoteEvent()
    data class ChangeColor(val color:Int):AddEditNoteEvent()
    object SaveNote:AddEditNoteEvent()
}
