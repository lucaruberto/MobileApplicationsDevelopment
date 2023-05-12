package it.polito.mad.lab3.db

import androidx.lifecycle.LiveData
import androidx.room.*
import it.polito.mad.lab4.db.FasciaOraria
import it.polito.mad.lab4.db.PlayGrounds
import java.util.*

@Dao
interface FasciaOrariaDao {
    @Query("SELECT * FROM fasciaoraria")
    fun getAllFasciaOraria() : LiveData<List<FasciaOraria>>

    @Query("SELECT * FROM fasciaoraria WHERE oraInizio NOT IN (" +
            "SELECT oraInizio " +
            "FROM reservations " +
            "WHERE playgroundName = :playground AND date = :date" +
            ")" )
    fun getFreeSlots(playground: String, date: Date): LiveData<List<FasciaOraria>>

    @Insert
    fun save(playground: PlayGrounds)

    @Update
    fun update(playground: PlayGrounds)

    @Delete
    fun delete(playground: PlayGrounds)
}