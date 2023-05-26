package it.polito.mad.lab3.db

import androidx.lifecycle.LiveData
import androidx.room.*
import it.polito.mad.lab5.db.FasciaOraria
import it.polito.mad.lab5.db.PlayGrounds
import java.util.*

@Dao
interface FasciaOrariaDao {

    

    @Insert
    fun save(playground: PlayGrounds)

    @Update
    fun update(playground: PlayGrounds)

    @Delete
    fun delete(playground: PlayGrounds)
}