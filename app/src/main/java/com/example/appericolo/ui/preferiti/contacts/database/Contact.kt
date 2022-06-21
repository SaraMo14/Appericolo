package com.example.appericolo.ui.preferiti.contacts.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Classe che modella un contatto telefonico dell'utente
 */
@Entity(tableName = "contacts_table")

 class Contact {
    @PrimaryKey @ColumnInfo(name = "number")
    var number: String
        get() {
            return field
        }

        set(value) {
            field = value
        }
    @ColumnInfo(name = "name")
    var name: String
        get() {
            return field
        }

        set(value) {
            field = value
        }

    constructor() : this("", "null")
    constructor(number: String, name: String){
        this.number = number
        this.name= name
    }

}