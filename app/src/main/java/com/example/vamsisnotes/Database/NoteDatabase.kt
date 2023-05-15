package com.example.vamsisnotes.Database

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vamsisnotes.Models.Note
import com.example.vamsisnotes.utilities.DATABASE_NAME

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun getNoteDao(): RoomDao

    companion object {
        private var INSTANCE: NoteDatabase? = null
        fun getDatabase(context: Context) : NoteDatabase{
            return INSTANCE?: synchronized(NoteDatabase::class){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                INSTANCE = instance
                instance
                }
            }
        }

    }
