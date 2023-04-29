package it.polito.mad.livedatatest.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase: RoomDatabase() {
    abstract fun itemDao(): ItemDAO

    companion object {
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        fun getDatabase(context: Context): ItemDatabase =
            (
                    INSTANCE?:
                    synchronized(this){
                        val i = INSTANCE ?: Room.databaseBuilder(
                            context.applicationContext,
                            ItemDatabase::class.java,
                            "items"
                        ).build()
                        INSTANCE = i
                        INSTANCE
                    }
            ) !!
    }
}