package it.polito.mad.lab5.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
data class PlayGrounds(
    //@PrimaryKey()
    //val id: Int ,

    val sportname: String,

    val playgroundName : String
    )
