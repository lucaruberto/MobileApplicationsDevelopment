package it.polito.mad.lab5.reservation

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import it.polito.mad.lab5.authentication.MyAuthenticationViewModel
import it.polito.mad.lab5.db.GlobalDatabase
import it.polito.mad.lab5.db.Reservation
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import kotlin.concurrent.thread

class ShowReservationsViewModel(application: Application) : AndroidViewModel(application) {
    val db = Firebase.firestore

    val reservations = mutableStateListOf<Reservation>()

    fun loadReservations() {
        viewModelScope.launch {
            db.collection("Reservations/${Firebase.auth.uid}")
                .get()
                .addOnSuccessListener {
                    for (doc in it.documents){
                        reservations.add(doc.toObject(Reservation::class.java)!!)
                    }
                    Log.d(TAG, "Reservations received: ${it.size()}")

                }
                .addOnFailureListener{
                    Log.d(TAG, "Error loading reservations: $it")
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
}