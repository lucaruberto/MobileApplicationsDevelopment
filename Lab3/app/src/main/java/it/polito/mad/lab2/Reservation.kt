package it.polito.mad.lab2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "discipline")
    val discipline: String
)
