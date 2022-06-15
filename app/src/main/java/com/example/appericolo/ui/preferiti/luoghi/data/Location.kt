package com.example.appericolo.ui.preferiti.luoghi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "indirizzo") val indirizzo: String?,
    @ColumnInfo(name = "latitude") val latitude: String?,
    @ColumnInfo(name = "longitude") val longitude: String?
)