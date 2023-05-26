package it.polito.mad.lab5.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity
data class Reservation(
    @PrimaryKey
    val id: String,
    val date: Date,
    val time: String,
    val discipline: String,
    val oraInizio: Int,
    val oraFine : Int,
    val playgroundName : String,
    val customRequest : String
)
