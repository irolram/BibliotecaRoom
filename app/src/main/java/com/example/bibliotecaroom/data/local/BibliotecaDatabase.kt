package com.example.bibliotecaroom.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bibliotecaroom.data.repository.LibroDao
import com.example.bibliotecaroom.domain.model.LibroEntity

//Configuración de la base de datos
@Database(entities = [LibroEntity::class], version = 1, exportSchema = false)

abstract class BibliotecaDatabase : RoomDatabase() {
    abstract fun libroDao(): LibroDao

    companion object {
        //Esta anotación garantiza que el valor de INSTANCE esté
        // siempre actualizado y visible para todos los hilos de ejecución de forma inmediata.
        @Volatile
        private var INSTANCE: BibliotecaDatabase? = null

        //Función para obtener la base de datos
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