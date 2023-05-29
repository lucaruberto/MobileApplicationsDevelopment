package it.polito.mad.lab5.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
class FasciaOraria(val oraInizio: Int, val oraFine: Int){
    constructor(): this(-1,-1)
}
