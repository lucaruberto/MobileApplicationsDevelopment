package it.polito.mad.lab4.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RatingDAO {
    @Query("SELECT * FROM ratings")
    fun getAll(): LiveData<List<Rating>>

    @Query("SELECT * FROM ratings WHERE fieldName=:fieldName")
    fun getAllReviewsForField(fieldName: String): LiveData<List<Rating>>


    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveReview(review: Rating)

    @Delete
    fun deleteReview(review: Rating)
}