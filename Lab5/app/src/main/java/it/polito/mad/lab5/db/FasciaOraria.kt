package it.polito.mad.lab5.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fasciaoraria")
data class FasciaOraria(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "oraInizio")
    val oraInizio: Int,

    @ColumnInfo(name="oraFine")
    val oraFine : Int
)
