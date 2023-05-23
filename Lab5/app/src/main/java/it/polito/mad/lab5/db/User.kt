package it.polito.mad.lab5.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "Nickname")
    var nickname: String,

    @ColumnInfo(name = "FullName")
    var fullname: String,

    @ColumnInfo(name = "Mail")
    var mail: String,

    @ColumnInfo(name = "Birthdate")
    var birthdate: String,

    @ColumnInfo(name = "Sex")
    var sex: String,

    @ColumnInfo(name = "City")
    var city: String

)
