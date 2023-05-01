package it.polito.mad.lab2.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface FasciaOrariaDao {
    @Query("SELECT * FROM fasciaoraria")
    fun getAllFasciaOraria() : LiveData<List<FasciaOraria>>

    @Query("SELECT * FROM fasciaoraria WHERE oraInizio NOT IN (" +
            "SELECT oraInizio " +
            "FROM reservations " +
            "WHERE playgroundName LIKE :playground AND date LIKE :date" +
            ")" )
    fun getFreeSlots(playground: String, date: Date): LiveData<List<FasciaOraria>>

    @Insert
    fun save(playground: PlayGrounds)

    @Update
    fun update(playground: PlayGrounds)

    @Delete
    fun delete(playground: PlayGrounds)
}