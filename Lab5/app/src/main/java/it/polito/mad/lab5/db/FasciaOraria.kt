package it.polito.mad.lab5.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FasciaOraria(
    @PrimaryKey
    val id: Int? = null,

    val oraInizio: Int? = null,

    val oraFine: Int? = null
)
