package com.example.noteapp.feature_note.presentation.notes

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.util.NoteOrder

sealed class NoteEvent{
    data class OrderEvent(val noteOrder: NoteOrder):NoteEvent()
    data class DeleteNoteEvent(val note: Note):NoteEvent()
    object RestoreNoteEvent:NoteEvent()
    object ToggleSectionEvent:NoteEvent()
}