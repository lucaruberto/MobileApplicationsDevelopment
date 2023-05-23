package it.polito.mad.lab5.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ratings")
data class Rating (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "reviewText")
    val reviewText: String,

    @ColumnInfo(name = "fieldName")
    val fieldName: String

)
