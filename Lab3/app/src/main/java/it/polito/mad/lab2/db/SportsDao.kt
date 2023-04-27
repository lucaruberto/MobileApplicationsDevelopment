package it.polito.mad.lab2.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SportsDao {
    @Query("SELECT discipline FROM sports")
    fun getAll() : LiveData<List<String>>

    @Insert
    fun save(sport: Sports)

    @Update
    fun update(sport: Sports)

    @Delete
    fun delete(sport: Sports)
}