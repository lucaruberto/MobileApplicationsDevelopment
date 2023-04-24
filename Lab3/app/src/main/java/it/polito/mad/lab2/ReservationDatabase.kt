package it.polito.mad.lab2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Reservation::class,Sports::class], version = 1)
abstract class ReservationDatabase: RoomDatabase() {
    abstract  fun reservationDao(): ReservationDao
    abstract fun sportsDao():SportsDao
    companion object {
        @Volatile
        private var INSTANCE: ReservationDatabase? = null

        fun getDatabase(context: Context): ReservationDatabase =
            (INSTANCE ?:
            synchronized(this) {
                val i = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ReservationDatabase::class.java,
                    "reservation_database").createFromAsset("db/reservation.db").build()
                INSTANCE = i
                INSTANCE
            })!!
    }
}