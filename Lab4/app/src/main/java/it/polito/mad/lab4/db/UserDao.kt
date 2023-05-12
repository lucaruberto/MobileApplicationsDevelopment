package it.polito.mad.lab3.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserbyId(id:Int) : LiveData<User>

    @Insert
    fun save(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}