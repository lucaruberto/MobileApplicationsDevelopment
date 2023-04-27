package it.polito.mad.lab2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Reservation::class, Sports::class], version = 1)
abstract class GlobalDatabase: RoomDatabase() {
    abstract  fun reservationDao(): ReservationDao
    abstract fun sportsDao(): SportsDao
    companion object {
        @Volatile
        private var INSTANCE: GlobalDatabase? = null

        fun getDatabase(context: Context): GlobalDatabase =
            (INSTANCE ?:
            synchronized(this) {
                val i = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GlobalDatabase::class.java,
                    "reservation_database").
                createFromAsset("db/reservation.db").
                build()
                INSTANCE = i
                INSTANCE
            })!!
    }
}