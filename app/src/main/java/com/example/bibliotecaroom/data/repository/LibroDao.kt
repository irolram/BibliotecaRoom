package com.example.bibliotecaroom.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bibliotecaroom.domain.model.LibroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LibroDao {
    @Query("SELECT * FROM libros")
    fun obtenerTodos(): Flow<List<LibroEntity>>

    @Query("SELECT * FROM libros WHERE id = :id")
    fun obtenerPorId(id: Int): Flow<LibroEntity?>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertar(libro: LibroEntity)

    @Update
    suspend fun actualizar(libro: LibroEntity)

    @Delete
    suspend fun eliminar(libro: LibroEntity)

    @Query("UPDATE libros SET disponible = :estaPrestado WHERE id = :id")
    suspend fun actualizarEstadoPrestamo(id: Int, estaPrestado: Boolean)
}