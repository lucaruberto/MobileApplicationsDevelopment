package it.polito.mad.lab2.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "reservations")
data class Reservation(



    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: Calendar,

    //AGGIUNGERE PLAYGROUND


    //DUE CAMPI
    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "discipline")
    val discipline: String
)
