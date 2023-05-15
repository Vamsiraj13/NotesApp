package com.example.vamsisnotes.Database


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vamsisnotes.Models.Note

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("UPDATE notes_table SET title = :title, description = :note WHERE id = :id")
    suspend fun update(id: Int?, title: String?, note: String?)
}