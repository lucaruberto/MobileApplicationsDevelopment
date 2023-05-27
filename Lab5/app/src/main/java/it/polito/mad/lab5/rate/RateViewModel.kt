package it.polito.mad.lab5.rate

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.mad.lab5.db.Rating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RateViewModel(application: Application): AndroidViewModel(application) {

    val db = FirebaseFirestore.getInstance()
    //val db = GlobalDatabase.getDatabase(application.applicationContext)

    private val fieldsNotRated = MutableLiveData<List<String>>()
    private var reviews = MutableLiveData<List<Rating>>()
    //private var fieldsNotRated: LiveData<List<String>> = db.rateDao().getFieldsWithoutRating()

    fun fetchAllReviews(): LiveData<List<Rating>> {
        viewModelScope.launch(Dispatchers.IO){
            db.collection("Reviews")
                .get()
                .addOnSuccessListener {reviewDocuments ->
                    reviews.value = reviewDocuments.map{
                        it.toObject(Rating::class.java)
                    }
                }
                .addOnFailureListener { ex ->
                    println("Exception while retrieving reviews: $ex")
                }



        }
        return reviews
    }

    fun fetchFieldsNotRated(): LiveData<List<String>> {
        val allFields = mutableListOf<String>()
        val reviewedFields = mutableListOf<String>()

        viewModelScope.launch(Dispatchers.IO) {
            db.collection("Playground")
                .get()
                .addOnSuccessListener {docs ->
                    docs.forEach {
                        it.getString("playgroundName")?.let { fieldName -> allFields.add(fieldName) }
                    }
                }
                .addOnFailureListener { ex ->
                    println("Exception while retrieving all field names: $ex")
                }

            db.collection("Reviews")
                .get()
                .addOnSuccessListener {docs ->
                    docs.forEach {
                        it.getString("Field")?.let { fieldName -> reviewedFields.add(fieldName) }
                    }
                }
                .addOnFailureListener { ex ->
                    println("Exception while retrieving reviewed field names: $ex")
                }

            fieldsNotRated.value = allFields.filter { reviewedFields.contains(it) } //don't add fieldname if it is already reviewed
        }
        return fieldsNotRated
    }

    fun addReview(review: Rating) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("Reviews")
                .add(review)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "Review added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { ex ->
                    Log.w(ContentValues.TAG, "Error adding review: $ex")
                }
        }
    }

    fun removeReview(review: Rating) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("Reviews")
                .document(review.id)
                .delete()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Review deleted with ID: $review.id")
                }
                .addOnFailureListener { ex ->
                    Log.w(ContentValues.TAG, "Error deleting review: $ex")
                }
        }
    }

    /*fun getAllReviews(): LiveData<List<Rating>> {
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
    }*/
}