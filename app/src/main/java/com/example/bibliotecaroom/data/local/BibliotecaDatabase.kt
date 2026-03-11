package com.example.bibliotecaroom.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bibliotecaroom.data.repository.LibroDao
import com.example.bibliotecaroom.domain.model.LibroEntity

@Database(entities = [LibroEntity::class], version = 1, exportSchema = false)
abstract class BibliotecaDatabase : RoomDatabase() {
    abstract fun libroDao(): LibroDao

    companion object {
        @Volatile
        private var INSTANCE: BibliotecaDatabase? = null

        fun getDatabase(context: Context): BibliotecaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BibliotecaDatabase::class.java,
                    "biblioteca_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}