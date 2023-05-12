package it.polito.mad.lab4.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reservations")
data class Reservation(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "discipline")
    val discipline: String,

    @ColumnInfo(name = "oraInizio")
    val oraInizio: Int,

    @ColumnInfo(name="oraFine")
    val oraFine : Int,

    @ColumnInfo(name="playgroundName")
    val playgroundName : String,

    @ColumnInfo(name="customRequest")
    val customRequest : String

)
