package com.example.noteapp.feature_note.data.source;

import androidx.room.*
import com.example.noteapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.selects.select

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note:Note)

    @Query("select * from note")
    fun getNotes(): Flow<List<Note>>

    @Query("select * from note where id= :id")
    fun getNodeById(id:Long):Note?
}
