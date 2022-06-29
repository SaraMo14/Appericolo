package com.example.appericolo.ui.preferiti.luoghi.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    // Flow rappresenta un flusso di dati che può essere ottenuto in modo asincrono
    fun getAll(): Flow<List<Location>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location)

    @Delete
    fun delete(location: Location)

    @Query("DELETE FROM location")
    suspend fun deleteAll()
}