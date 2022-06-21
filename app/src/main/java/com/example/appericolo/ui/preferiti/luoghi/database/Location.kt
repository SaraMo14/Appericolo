package com.example.appericolo.ui.preferiti.luoghi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Classe che modella un luogo preferito dell'utente
 */
@Entity
data class Location(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "indirizzo") val indirizzo: String?,
    @ColumnInfo(name = "latitude") val latitude: String?,
    @ColumnInfo(name = "longitude") val longitude: String?
)