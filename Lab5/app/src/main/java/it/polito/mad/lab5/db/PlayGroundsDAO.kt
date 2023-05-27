package it.polito.mad.lab5.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlayGroundsDAO {

    //fun getPlayGroundsbySportName(name:String) : LiveData<List<String>>

    @Insert
    fun save(playground: PlayGrounds)

    @Update
    fun update(playground: PlayGrounds)

    @Delete
    fun delete(playground: PlayGrounds)
}