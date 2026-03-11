package com.example.bibliotecaroom.di

import android.content.Context
import com.example.bibliotecaroom.data.local.BibliotecaDatabase
import com.example.bibliotecaroom.data.repository.BibliotecaRepositoryImpl
import com.example.bibliotecaroom.data.repository.IBibliotecaRepository
import com.example.bibliotecaroom.data.repository.LibroDao
import com.example.bibliotecaroom.domain.usecase.InsertarLibroUseCase
import com.example.bibliotecaroom.domain.usecase.PrestarLibroUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Modulo para proveer dependencias
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Proveemos la base de datos
    @Provides
    @Singleton
    fun provideBibliotecaDatabase(@ApplicationContext context: Context): BibliotecaDatabase {
        return BibliotecaDatabase.getDatabase(context)
    }

    // Proveemos el DAO
    @Provides
    @Singleton
    fun provideLibroDao(database: BibliotecaDatabase): LibroDao {
        return database.libroDao()
    }
    // Proveemos el repositorio
    @Provides
    @Singleton
    fun provideBibliotecaRepository(libroDao: LibroDao): IBibliotecaRepository {
        // Devolvemos la interfaz, pero instanciamos la IMPLEMENTACIÓN
        return BibliotecaRepositoryImpl(libroDao)
    }

    // Proveemos los casos de uso
    @Provides
    @Singleton
    fun provideInsertarLibroUseCase(repository: IBibliotecaRepository): InsertarLibroUseCase {
        return InsertarLibroUseCase(repository)
    }

    // Proveemos el caso de uso para prestar un libro
    @Provides
    @Singleton
    fun providePrestarLibroUseCase(repository: IBibliotecaRepository): PrestarLibroUseCase {
        return PrestarLibroUseCase(repository)
    }
}