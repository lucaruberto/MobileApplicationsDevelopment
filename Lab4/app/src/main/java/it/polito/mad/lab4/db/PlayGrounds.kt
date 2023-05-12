package it.polito.mad.lab4.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playgrounds")
data class PlayGrounds(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "sportname")
    val sportname: String,

    @ColumnInfo(name="playgroundName")
    val playgroundName : String
    )
