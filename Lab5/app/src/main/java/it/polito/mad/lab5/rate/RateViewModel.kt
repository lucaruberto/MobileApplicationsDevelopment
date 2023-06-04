package it.polito.mad.lab5.rate

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import it.polito.mad.lab5.db.PlayGrounds
import it.polito.mad.lab5.db.ProvaUser
import it.polito.mad.lab5.db.Rating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RateViewModel(application: Application): AndroidViewModel(application) {
    val db = FirebaseFirestore.getInstance()

    private val fieldsNotRated = MutableLiveData<List<String>>()
    private var reviews = MutableLiveData<List<Rating>>()
    private var allFields = MutableLiveData<List<PlayGrounds>>()
    private var loggedUser = MutableLiveData<ProvaUser>()

    fun fetchAllReviews(): LiveData<List<Rating>> {
        viewModelScope.launch(Dispatchers.IO){
            db.collection("Review")
                .get()
                .addOnSuccessListener {reviewDocuments ->
                    val reviewsList = reviewDocuments.map{
                        val r = it.toObject(Rating::class.java)
                        r.id = it.id
                        r
                    }
                    reviews.postValue(reviewsList)
                }
                .addOnFailureListener { ex ->
                    println("Exception while retrieving reviews: $ex")
                }
        }
        return reviews
    }

    fun fetchAllFields(): LiveData<List<PlayGrounds>> {
        val fields = mutableListOf<PlayGrounds>()
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("Playground")
                .get()
                .addOnSuccessListener { docsAll ->
                    docsAll.map {
                        fields.add(it.toObject(PlayGrounds::class.java))
                    }
                    allFields.postValue(fields)
                }
        }
        return allFields
    }

    fun fetchFieldsNotRated(): LiveData<List<String>> {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("Playground")
                .get()
                .addOnSuccessListener {docsAll ->
                    val allFields = docsAll.map {
                        it.getString("playgroundName")!!
                    }
                    db.collection("Review")
                        .get()
                        .addOnSuccessListener {docsRev ->
                            val reviewedFields = docsRev.map {
                                it.getString("field")!!
                            }
                            val fieldsToAdd = allFields.filter { !reviewedFields.contains(it) }
                            fieldsNotRated.postValue(fieldsToAdd)
                        }
                        .addOnFailureListener { ex ->
                            println("Exception while retrieving all field names: $ex")
                        }
                }
                .addOnFailureListener { ex ->
                    println("Exception while retrieving reviewed field names: $ex")
                }
        }
        return fieldsNotRated
    }

    fun addReview(field: String, reviewText: String, rating: Int, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userDocument = db.collection("Users")
                .document(userId)
                .get()
                .await()
            val user = userDocument.toObject(ProvaUser::class.java)
            val reviewToAdd = Rating("", field, reviewText, rating, user)
            db.collection("Review")
                .add(reviewToAdd)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "Review added with ID: ${documentReference.id}")
                    fetchAllReviews()
                }
                .addOnFailureListener { ex ->
                    Log.w(ContentValues.TAG, "Error adding review: $ex")
                }
        }
    }

    fun fetchLoggedUser(): LiveData<ProvaUser> {
        val userId = Firebase.auth.uid!!
        viewModelScope.launch(Dispatchers.IO) {
            val userDocument = db.collection("Users")
                .document(userId)
                .get()
                .await()
            loggedUser.postValue(userDocument.toObject(ProvaUser::class.java))
        }
        return loggedUser
    }

    fun removeReview(reviewId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("Review")
                .document(reviewId)
                .delete()
                .addOnSuccessListener {
                    fetchAllReviews()
                    Log.d(ContentValues.TAG, "Review deleted with ID: $reviewId")
                }
                .addOnFailureListener { ex ->
                    Log.w(ContentValues.TAG, "Error deleting review: $ex")
                }
        }
    }
}