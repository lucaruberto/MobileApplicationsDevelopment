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
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE reservations ADD COLUMN customRequest TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): GlobalDatabase =
            (INSTANCE ?:
            synchronized(this) {
                val i = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GlobalDatabase::class.java,
                    "reservation_database").
                createFromAsset("db/reservation.db").
                addMigrations(MIGRATION_1_2).
                build()
                INSTANCE = i
                INSTANCE
            })!!
    }
}