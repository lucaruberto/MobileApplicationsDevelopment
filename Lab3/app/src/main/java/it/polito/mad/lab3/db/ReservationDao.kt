package it.polito.mad.lab3.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.Date

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations")
    fun getAll() : LiveData<List<Reservation>>

    @Query("SELECT * FROM reservations WHERE id IN (:reservationIds)")
    fun loadAllByIds(reservationIds: List<Int>): LiveData<List<Reservation>>

    @Query("SELECT * FROM reservations WHERE date LIKE :date")
    fun loadAllByDate(date: Date): LiveData<List<Reservation>>

    @Query("SELECT date FROM reservations")
    fun loadAllDate(): LiveData<List<Date>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun save(reservation: Reservation)

    @Update
    fun update(reservation: Reservation)

    @Delete
    fun delete(reservation: Reservation)

}