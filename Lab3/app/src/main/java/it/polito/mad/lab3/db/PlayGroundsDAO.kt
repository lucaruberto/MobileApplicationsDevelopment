package it.polito.mad.lab3.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlayGroundsDAO {
    @Query("SELECT playgroundName FROM playgrounds WHERE sportname LIKE :name")
    fun getPlayGroundsbySportName(name:String) : LiveData<List<String>>

    @Insert
    fun save(playground: PlayGrounds)

    @Update
    fun update(playground: PlayGrounds)

    @Delete
    fun delete(playground: PlayGrounds)
}