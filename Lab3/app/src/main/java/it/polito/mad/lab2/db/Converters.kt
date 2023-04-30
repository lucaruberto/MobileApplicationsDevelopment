package it.polito.mad.lab2.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*


class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(value) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {

        val x=SimpleDateFormat("dd-MM-yyyy");
        val y= x.format(date);
        val data: Date=x.parse(y)
        return data.time;
    }
}