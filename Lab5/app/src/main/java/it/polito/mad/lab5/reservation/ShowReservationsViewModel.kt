package it.polito.mad.lab5.reservation

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
    var showDialog = mutableStateOf(false)

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
                                    val newReservation = dc.document.toObject(Reservation::class.java)

                                    //check if it is an invitation reservationUid != uid
                                    if(newReservation.userId != Firebase.auth.uid){
                                        //it's an invitation
                                        db.collection("Reservations").document("${newReservation.reservationId}")
                                            .get()
                                            .addOnSuccessListener {
                                                //invitation still exists
                                                if(it.exists())
                                                {
                                                    reservations.add(newReservation)

                                                }
                                                else{
                                                    db.collection("Users/${Firebase.auth.uid}/Reservations")
                                                        .document(dc.document.id).delete()
                                                }
                                            }
                                    }
                                    else{
                                        // not an invitation, is one of my reservations
                                        reservations.add(newReservation)
                                    }
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    val updatedRes = dc.document.toObject(Reservation::class.java)
                                    reservations.removeIf {
                                        it.reservationId == updatedRes.reservationId
                                    }
                                    reservations.add(updatedRes)
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

    fun sendInvitation(reservationToSend: Reservation, userIdToSend: String){
        viewModelScope.launch {
            db.collection("Users/$userIdToSend/Invitations")
                //add to userIdToSend Invitations collections an invitations for my reservation
                .add(reservationToSend)
                .addOnSuccessListener {
                    Log.d(TAG, "Invitation sent to $userIdToSend for reservation ${reservationToSend.reservationId}")
                }
                .addOnFailureListener {
                    Log.w(TAG, "Error sending invitation to $userIdToSend for reservation ${reservationToSend.reservationId}")
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
                .addOnSuccessListener{ reservationDocuments ->// delete from User/Reservations
                    val reservationToDelete = reservationDocuments.documents[0].toObject(Reservation::class.java)
                    val resId = reservationToDelete!!.reservationId
                    db.collection("Users/${Firebase.auth.uid}/Reservations")
                        .document(reservationDocuments.documents[0].id)
                        .delete()
                        .addOnSuccessListener {

                            //check if it's one of my reservations
                            if(reservationToDelete.userId == Firebase.auth.uid){
                                //one of my reservations, must delete also from 'Reservations' collection
                                db.collection("Reservations").document(resId).delete()
                                    .addOnSuccessListener{
                                        Log.d(TAG, "Reservation deleted successfully from 'Reservations' and 'User/Reservations'")
                                    }
                                    .addOnFailureListener { e ->
                                        db.collection("Users/${Firebase.auth.uid}/Reservations")
                                            .add(reservationToDelete)
                                        Log.w(TAG, "Error deleting reservation from 'Reservations': $e")
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            db.collection("Reservations").document(resId).set(reservationToDelete)
                            Log.w(TAG, "Error deleting reservation from 'User/Reservations': $e")
                        }

                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error fetching reservation to delete: $e")
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        reg.remove()
    }
}