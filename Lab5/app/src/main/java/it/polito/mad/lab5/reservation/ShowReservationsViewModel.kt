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

    override fun onCleared() {
        super.onCleared()
        reg.remove()
    }
}