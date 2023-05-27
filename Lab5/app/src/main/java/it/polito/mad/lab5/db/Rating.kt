package it.polito.mad.lab5.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ratings")
data class Rating (
    @PrimaryKey
    val id: String,
    val field: String,
    val reviewText: String,
    //val score: Int,
    //val user: String
)
