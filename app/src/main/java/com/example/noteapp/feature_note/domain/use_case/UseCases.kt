package com.example.noteapp.feature_note.domain.use_case

data class UseCases(
    val deleteNoteUseCase: DeleteNoteUseCase,
    val getNotesUseCase: GetNotesUseCase,
    val addNoteUseCase:AddNoteUseCase,
    val getNoteUseCase: GetNoteUseCase
)
