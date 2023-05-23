package it.polito.mad.lab5.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(value) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        val x = SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
        val y = x.format(date);
        val data = x.parse(y) as Date
        return data.time
    }
}