package com.example.challengeroom1msbdnfaza.room

import androidx.room.*


@Dao
interface NoteDao {
   @Insert
    fun addNote (note: Note)

   @Update
    fun updateNote (note: Note)

   @Delete
    fun deleteNote (note: Note)

   @Query("SELECT * FROM note")
    fun getNotes(): List<Note>

   @Query("SELECT * FROM Note WHERE id =:note_Id")
   suspend fun getNote(note_Id:Int):List<Note>


}





