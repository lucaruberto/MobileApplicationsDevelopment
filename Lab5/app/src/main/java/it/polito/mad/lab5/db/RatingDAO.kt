package it.polito.mad.lab5.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RatingDAO {
    /*@Query("SELECT * FROM ratings")
    fun getAll(): LiveData<List<Rating>>

    @Query("SELECT * FROM ratings WHERE fieldName=:fieldName")
    fun getAllReviewsForField(fieldName: String): LiveData<List<Rating>>

    @Query("SELECT playgroundName " +
            "FROM playgrounds " +
            "WHERE playgroundName NOT IN (" +
            "   SELECT fieldName FROM ratings" +
            ") ")
    //should get all the names of the playgrounds that haven't been rated yet
    fun getFieldsWithoutRating(): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveReview(review: Rating)

    @Delete
    fun deleteReview(review: Rating)*/
}