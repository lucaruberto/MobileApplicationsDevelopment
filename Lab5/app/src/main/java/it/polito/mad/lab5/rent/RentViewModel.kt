package it.polito.mad.lab5.rent

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import it.polito.mad.lab5.db.FasciaOraria
import it.polito.mad.lab5.db.GlobalDatabase
import it.polito.mad.lab5.db.Reservation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import kotlin.concurrent.thread

class RentViewModel(application: Application) : AndroidViewModel(application) {
    var db = FirebaseFirestore.getInstance()

    val sportsList: MutableLiveData<List<String>> = MutableLiveData()
    val sportsListFlow: Flow<List<String>> = sportsList.asFlow()
    fun getFasceOrarieLibere(playground: String, date: Date): LiveData<List<FasciaOraria>> {
        val liveFasceLibere = MutableLiveData<List<FasciaOraria>>()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("reservations")
                    .whereEqualTo("playgroundName", playground)
                    .whereEqualTo("date", date)
                    .get()
                    .addOnSuccessListener { reservationDocuments ->
                        val reservedTimeSlots = reservationDocuments.map {
                            it.reference.path
                        }.toSet()

                        db.collection("TimeSlot")
                            .orderBy("oraInizio")
                            .get()
                            .addOnSuccessListener { timeSlotDocuments ->
                                val freeTimeSlots = timeSlotDocuments.filter {
                                    !reservedTimeSlots.contains(it.reference.path)
                                }.map { document ->
                                    document.toObject(FasciaOraria::class.java)
                                }
                                liveFasceLibere.postValue(freeTimeSlots)
                            }
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Exception occurred = $e")
            }
        }

        return liveFasceLibere
    }



    fun fetchAllSports() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val sportsDocuments = db.collection("Sport").get().await()
                val sportsNames = sportsDocuments.documents.mapNotNull { it.getString("discipline") }
                sportsList.postValue(sportsNames)
            } catch (e: Exception) {
                println("Exception occurred = $e")
            }
        }
    }



        fun getPlaygroundsbyName(sport:String): LiveData<List<String>> {
            val playgrounds = MutableLiveData<List<String>>()
            db.collection("Playground")
                .whereEqualTo("sportname", sport)
                .get()
                .addOnSuccessListener { documents ->
                    val playgroundsList = mutableListOf<String>()
                    for (document in documents) {
                        playgroundsList.add(document.getString("playgroundName") ?: "")
                    }
                    playgrounds.value = playgroundsList
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
            return playgrounds
        }
    fun getFullDates(playground: String): LiveData<List<Date>> {
        val fullDates = MutableLiveData<List<Date>>()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("reservations")
                    .whereEqualTo("playgroundName", playground)
                    .get()
                    .addOnSuccessListener { documents ->
                        val datesMap = mutableMapOf<Date, Int>()
                        for (document in documents) {
                            val reservation = document.toObject(Reservation::class.java)
                            val date = reservation.date // assuming 'date' field in Reservation class
                            datesMap[date] = datesMap.getOrDefault(date, 0) + 1
                        }
                        val fullDatesList = datesMap.filter { it.value >= 14 }.keys.toList()
                        fullDates.postValue(fullDatesList)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Exception occurred = $e")
            }
        }

        return fullDates
    }

    /*
    fun getRecyclerAdapter(fasce:List<FasciaOraria>,sport:String,field:String,date:Date,time:String):RentRecyclerViewAdapter{
        return RentRecyclerViewAdapter(fasce.map { x->it.polito.mad.lab5.reservation.ShowReservationModel(x.id,sport,field,date,time,x.oraInizio,x.oraFine, "") },date,sport,field,this);
    }*/

    fun saveReservation(reservation: Reservation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("Reservations/${Firebase.auth.uid}")
                    .add(reservation)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Exception occurred = $e")
            }
        }
    }

}