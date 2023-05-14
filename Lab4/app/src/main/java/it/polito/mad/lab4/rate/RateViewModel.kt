package it.polito.mad.lab4.rate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import it.polito.mad.lab4.db.GlobalDatabase
import it.polito.mad.lab4.db.Rating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RateViewModel(application: Application): AndroidViewModel(application) {
    val db = GlobalDatabase.getDatabase(application.applicationContext)
    private var fieldsNotRated: LiveData<List<String>> = db.rateDao().getFieldsWithoutRating()

    fun getAllReviews(): LiveData<List<Rating>> {
        return db.rateDao().getAll()
    }

    fun getAllReviewsForField(fieldName: String): LiveData<List<Rating>> {
        return db.rateDao().getAllReviewsForField(fieldName)
    }

    fun saveReview(review: Rating) {
        viewModelScope.launch(Dispatchers.IO){
            db.rateDao().saveReview(review)
        }
    }

    fun getFieldsNotRated() : LiveData<List<String>>{
        fieldsNotRated = db.rateDao().getFieldsWithoutRating()
        return fieldsNotRated
    }

    fun deleteReview(review : Rating) {
        viewModelScope.launch(Dispatchers.IO){
            db.rateDao().deleteReview(review)
        }
    }
}