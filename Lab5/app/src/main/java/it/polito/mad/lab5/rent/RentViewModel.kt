package it.polito.mad.lab5.rent

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import it.polito.mad.lab5.db.FasciaOraria
import it.polito.mad.lab5.db.ProvaSport
import it.polito.mad.lab5.db.Reservation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date


class RentViewModel(application: Application) : AndroidViewModel(application) {
    var db = FirebaseFirestore.getInstance()

    val sportsList1 = mutableStateListOf<String>()
    val playgroundsList = mutableStateListOf<String>()
    val fullDates = mutableListOf<Date>()
    val freeSlots = mutableStateListOf<FasciaOraria>()
    val selectedSport = mutableStateOf("Sport")
    val selectedPlayground = mutableStateOf("Playground")
    val selectedDate = mutableStateOf<Date?>(null)
    val selectedTimeSlot = mutableStateOf<FasciaOraria?>(null)
    val customRequest = mutableStateOf("")

    private lateinit var reg: ListenerRegistration
    private lateinit var regFullDates: ListenerRegistration
    private val datesMap = mutableMapOf<Date, Int>()

    fun resetValues(){
        selectedSport.value = "Sport"
        selectedPlayground.value = "Playground"
        selectedDate.value = null
        customRequest.value = ""
    }

    fun loadFreeSlots() {
        db.collection("TimeSlot")
            .orderBy("oraInizio")
            .get()
            .addOnSuccessListener { timeSlotDocument ->
                freeSlots.clear()
                for (tsd in timeSlotDocument){
                    val timeSlot = tsd.toObject<FasciaOraria>()
                    freeSlots.add(FasciaOraria(timeSlot.oraInizio,timeSlot.oraFine))
                }

                viewModelScope.launch(Dispatchers.IO) {
                    //reg = db.collection("Users/${Firebase.auth.uid}/Reservations")
                    reg = db.collection("Reservations")
                        .whereEqualTo("playgroundName", selectedPlayground.value)
                        .whereEqualTo("date", selectedDate.value)
                        .addSnapshotListener { snapshots, e ->
                            if (e != null) {
                                Log.w(TAG, "listen:error", e)
                            }
                            if(snapshots != null) {
                                for (dc in snapshots.documentChanges) {
                                    when (dc.type) {
                                        DocumentChange.Type.ADDED -> {
                                            Log.d(TAG, "New reservation: ${dc.document.data}")
                                            val reservationToDelete = dc.document.toObject(Reservation::class.java)
                                            freeSlots.removeIf {
                                                it.oraInizio == reservationToDelete.oraInizio
                                            }
                                        }

                                        DocumentChange.Type.MODIFIED -> {
                                            Log.d(TAG, "Modified reservation: ${dc.document.data}")
                                        }

                                        DocumentChange.Type.REMOVED -> {
                                            Log.d(TAG, "Removed reservation: ${dc.document.data}")
                                            val res = dc.document.toObject(Reservation::class.java)
                                            freeSlots.add(FasciaOraria(res.oraInizio, res.oraFine))
                                            freeSlots.sortBy { it.oraInizio }
                                        }
                                    }
                                }
                            }
                        }
                }
            }
            .addOnFailureListener{
                Log.w(TAG, "Error loading free slots: $it")
            }
    }



    fun fetchAllSports() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val sportsDocuments = db.collection("Sport").get().await()
                sportsList1.clear()
                for (sportDoc in sportsDocuments){
                    val sportName = sportDoc.toObject<ProvaSport>().discipline
                    sportsList1.add(sportName)
                }
            } catch (e: Exception) {
                println("Exception occurred = $e")
            }
        }
    }

    fun loadPlaygrounds() {
        db.collection("Playground")
            .whereEqualTo("sportName", selectedSport.value)
            .get()
            .addOnSuccessListener { documents ->
                //val playgroundsList = mutableListOf<String>()
                playgroundsList.clear()
                for (document in documents) {
                    Log.d(TAG, "Document fetched for playground: ${document.data}")
                    playgroundsList.add(document.getString("playgroundName") ?: "")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun loadFullDates() {
        datesMap.clear()
        fullDates.clear()
        viewModelScope.launch(Dispatchers.IO) {
            //reg = db.collection("Users/${Firebase.auth.uid}/Reservations")
            reg = db.collection("Reservations")
                .whereEqualTo("playgroundName", selectedPlayground.value)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w(TAG, "listen:error", e)
                    }
                    if(snapshots != null) {
                        for (dc in snapshots.documentChanges) {
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    Log.d(TAG, "New reservation: ${dc.document.data}")
                                    val reservation = dc.document.toObject(Reservation::class.java)
                                    val date = reservation.date // assuming 'date' field in Reservation class
                                    datesMap[date] = datesMap.getOrDefault(date, 0) + 1
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    Log.d(TAG, "Modified reservation: ${dc.document.data}")
                                }

                                DocumentChange.Type.REMOVED -> {
                                    Log.d(TAG, "Removed reservation: ${dc.document.data}")
                                    val reservation = dc.document.toObject(Reservation::class.java)
                                    val date = reservation.date // assuming 'date' field in Reservation class
                                    datesMap[date] = datesMap.getOrDefault(date, 1) - 1
                                }
                            }
                            fullDates.addAll(datesMap.filter { it.value >= 12 }.keys.toList())
                        }
                    }
                }
        }
    }

    fun saveReservation(/*reservation: Reservation*/) {
        val reservation = Reservation(
            reservationId = "",
            userId = Firebase.auth.uid!!,
            discipline = selectedSport.value,
            playgroundName = selectedPlayground.value,
            date = selectedDate.value!!,
            oraInizio = selectedTimeSlot.value!!.oraInizio,
            oraFine = selectedTimeSlot.value!!.oraFine,
            customRequest = customRequest.value
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("Reservations")
                    .add(reservation)
                    .addOnSuccessListener{
                        reservation.reservationId = it.id   //inside user reservation, save a pointer to the same reservation inside 'Reservations' collection

                        db.collection("Users/${Firebase.auth.uid}/Reservations")
                            .add(reservation)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                db.collection("Reference").document(it.id).delete()
                                Log.w(TAG, "Error adding document to 'User/Reservations': $e")
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document to 'Reservations': ", e)
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Exception occurred = $e")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        reg.remove()
        regFullDates.remove()
    }
}