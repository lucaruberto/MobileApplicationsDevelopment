package it.polito.mad.lab2

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SportsDao {
    @Query("SELECT * FROM sports")
    fun getAll() : LiveData<List<Sports>>

    @Insert
    fun save(sport: Sports)

    @Update
    fun update(sport: Sports)

    @Delete
    fun delete(sport: Sports)
}