package com.example.appericolo.ui.preferiti.luoghi.data

import androidx.room.*
import com.example.appericolo.ui.preferiti.luoghi.data.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    fun getAll(): Flow<List<Location>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location)

    @Delete
    fun delete(location: Location)

    @Query("DELETE FROM location")
    suspend fun deleteAll()
}