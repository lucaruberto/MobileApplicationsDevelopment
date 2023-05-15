package it.polito.mad.lab4.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SportsDao {
    @Query("SELECT discipline FROM sports")
    fun getAll() : LiveData<List<String>>

    @Query("SELECT * FROM sports")
    fun getSports():LiveData<List<Sports>>

    @Insert
    fun save(sport: Sports)

    @Update
    fun update(sport: Sports)

    @Delete
    fun delete(sport: Sports)
}