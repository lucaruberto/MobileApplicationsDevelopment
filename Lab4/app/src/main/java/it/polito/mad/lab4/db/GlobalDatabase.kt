package it.polito.mad.lab3.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Reservation::class, Sports::class,PlayGrounds::class,FasciaOraria::class], version = 2)
@TypeConverters(Converters::class)
abstract class GlobalDatabase: RoomDatabase() {
    abstract  fun reservationDao(): ReservationDao
    abstract fun sportsDao(): SportsDao

    abstract fun playgroundsDao():PlayGroundsDAO

    abstract fun fasciaorariaDao():FasciaOrariaDao


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