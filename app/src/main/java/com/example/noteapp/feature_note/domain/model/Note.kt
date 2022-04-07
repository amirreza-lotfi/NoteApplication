package com.example.noteapp.feature_note.domain.model

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteapp.ui.theme.*
import java.lang.Exception

@Entity(tableName = "note")
data class Note(
    @PrimaryKey
    val id: Long,
    val time:Long = 0L,
    val title:String = "",
    val content:String = "",
    val color:Int = noteColors[0].toArgb()
){
    companion object{
        val noteColors = listOf(RedOrange, RedPink, BabyBlue, Violet, LightGreen, White)

    }
}

class InvalidNoteException(message:String) : Exception(message)
