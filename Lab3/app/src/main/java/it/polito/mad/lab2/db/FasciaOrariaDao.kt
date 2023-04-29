package it.polito.mad.lab2.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FasciaOrariaDao {
    @Query("SELECT * FROM fasciaoraria")
    fun getAllFasciaOraria() : LiveData<List<FasciaOraria>>

    @Insert
    fun save(playground: PlayGrounds)

    @Update
    fun update(playground: PlayGrounds)

    @Delete
    fun delete(playground: PlayGrounds)
}