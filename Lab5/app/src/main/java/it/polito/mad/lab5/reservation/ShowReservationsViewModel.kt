package it.polito.mad.lab5.reservation

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import it.polito.mad.lab5.db.Reservation
import kotlinx.coroutines.launch
import java.util.Date

class ShowReservationsViewModel(application: Application) : AndroidViewModel(application) {
    val db = Firebase.firestore

    val reservations = mutableStateListOf<Reservation>()

    private lateinit var reg: ListenerRegistration

    fun loadReservations() {
        viewModelScope.launch {
            /*db.collection("Users/${Firebase.auth.uid}/Reservations")
                .get()
                .addOnSuccessListener {
                    reservations.clear()
                    for (doc in it.documents){
                        reservations.add(doc.toObject(Reservation::class.java)!!)
                    }
                    Log.d(TAG, "Reservations received: ${it.size()}")

                }
                .addOnFailureListener{
                    Log.d(TAG, "Error loading reservations: $it")
                }
            */
            reservations.clear()
            reg = db.collection("Users/${Firebase.auth.uid}/Reservations")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w(TAG, "listen:error", e)
                    }
                    if(snapshots != null) {
                        for (dc in snapshots.documentChanges) {
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    Log.d(TAG, "New reservation: ${dc.document.data}")
                                    reservations.add(dc.document.toObject(Reservation::class.java))
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    Log.d(TAG, "Modified reservation: ${dc.document.data}")
                                }

                                DocumentChange.Type.REMOVED -> {
                                    Log.d(TAG, "Removed reservation: ${dc.document.data}")
                                    val reservationToDelete = dc.document.toObject(Reservation::class.java)
                                    reservations.removeIf {
                                        it.date == reservationToDelete.date &&
                                        it.oraInizio == reservationToDelete.oraInizio &&
                                        it.playgroundName == reservationToDelete.playgroundName &&
                                        it.discipline == reservationToDelete.discipline
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    fun deleteReservation(date: Date, discipline: String, startHour: Int, playgroundName: String) {
        viewModelScope.launch {
            db.collection("Users/${Firebase.auth.uid}/Reservations")
                .whereEqualTo("discipline", discipline)
                .whereEqualTo("date", date)
                .whereEqualTo("playgroundName", playgroundName)
                .whereEqualTo("oraInizio", startHour)
                .get()
                .addOnSuccessListener{ it ->
                    db.collection("Users/${Firebase.auth.uid}/Reservations")
                        .document(it.documents[0].id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Reservation deleted successfully")
                        }
                        .addOnFailureListener {
                            Log.w(TAG, "Error deleting reservation: $it")
                        }
                }
        }
    }

    /*
    private val db = GlobalDatabase.getDatabase(getApplication<Application>().applicationContext)
    private var liveDates = db.reservationDao().loadAllDate()

    fun getReservationFromDate(d: Date): LiveData<List<Reservation>> {
       return db.reservationDao().loadAllByDate(d)
    }

    fun getLiveDates(): LiveData<List<Date>> {
        //read again to check for changes instead of using mutable list
        liveDates = db.reservationDao().loadAllDate()
        return liveDates
    }

    fun deleteReservation(id:Int,date:Date,time:String,discipline:String,starthour:Int,endhour:Int,playgroundName:String, customRequest: String) {
        thread { db.reservationDao().delete(Reservation(id,date,time,discipline, starthour,endhour,playgroundName, customRequest )) }

    }
/*
    fun getRecyclerAdapter(lista:List<Reservation>, date:Date):ReservationRecyclerViewAdapter{
        return ReservationRecyclerViewAdapter(lista.map { x-> ShowReservationModel(x.id,x.discipline,x.playgroundName,x.date,x.time,x.oraInizio,x.oraFine, x.customRequest) },date,this)
    }*/
    +/
     */

    override fun onCleared() {
        super.onCleared()
        reg.remove()
    }
}