package it.polito.mad.lab2.db

import android.content.Context
import androidx.room.*

@Database(entities = [Reservation::class, Sports::class,PlayGrounds::class,FasciaOraria::class], version = 1)
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