package it.polito.mad.lab2.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations")
    fun getAll() : LiveData<List<Reservation>>

    @Query("SELECT * FROM reservations WHERE id IN (:reservationIds)")
    fun loadAllByIds(reservationIds: List<Int>): LiveData<List<Reservation>>

    @Query("SELECT * FROM reservations WHERE date LIKE :date")
    fun loadAllByDate(date:String): LiveData<List<Reservation>>

    @Insert()
    fun save(reservation: Reservation)

    @Update
    fun update(reservation: Reservation)

    @Delete
    fun delete(reservation: Reservation)

}