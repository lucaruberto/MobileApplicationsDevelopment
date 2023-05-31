package it.polito.mad.lab5.rent

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


class RentViewModel(application: Application) : AndroidViewModel(application) {
    var db = FirebaseFirestore.getInstance()

    //val sportsList: MutableLiveData<List<String>> = MutableLiveData()
    //val sportsListFlow: Flow<List<String>> = sportsList.asFlow()

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

    fun loadFreeSlots(/*playground: String, date: Date*/)/*: LiveData<List<FasciaOraria>> */{
        //
/*
        val localDate: LocalDate = selectedDate.value!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val year: Int = localDate.year
        val month: Int = localDate.monthValue
        val day: Int = localDate.dayOfMonth


*/

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
                    reg = db.collection("Users/${Firebase.auth.uid}/Reservations")
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
                                        }
                                    }
                                }
                            }
                        }
                    /*try {
                        //db.collection("Reservations/${selectedPlayground.value}"/*/$year-$month/$day"*/)
                        db.collection("Users/${Firebase.auth.uid}/Reservations")
                            .whereEqualTo("playgroundName", selectedPlayground.value)
                            .whereEqualTo("date", selectedDate.value)
                            .get()
                            .addOnSuccessListener { reservations ->
                                val reservedSlotsStartHour = mutableListOf<Int>()

                                for (res in reservations){
                                    val res = res.toObject<Reservation>()
                                    reservedSlotsStartHour.add(res.oraInizio)
                                }
                                /*val reservedTimeSlots = reservationsTimeSlots.map {
                                    it.reference.path
                                }.toSet()*/


                                db.collection("TimeSlot")
                                    .orderBy("oraInizio")
                                    .get()
                                    .addOnSuccessListener { timeSlotDocument ->
                                        freeSlots.clear()
                                        for (tsd in timeSlotDocument){
                                            val timeSlot = tsd.toObject<FasciaOraria>()
                                            if(!reservedSlotsStartHour.contains(timeSlot.oraInizio))
                                                freeSlots.add(FasciaOraria(timeSlot.oraInizio,timeSlot.oraFine))
                                        }
                                        /*val freeTimeSlots = timeSlotDocuments.filter {
                                            !reservedTimeSlots.contains(it.reference.path)
                                        }.map { document ->
                                            document.toObject(FasciaOraria::class.java)
                                        }
                                        liveFasceLibere.postValue(freeTimeSlots)*/
                                    }
                            }
                    } catch (e: Exception) {
                        Log.w(TAG, "Exception occurred = $e")
                    }*/
                }
            }
            .addOnFailureListener{
                Log.w(TAG, "Error loading free slots: $it")
            }





        //return liveFasceLibere
    }



    fun fetchAllSports() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val sportsDocuments = db.collection("Sport").get().await()
                /*
                val sportsNames = sportsDocuments.documents.mapNotNull { it.getString("discipline") }
                sportsList.postValue(sportsNames)
                */
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



    //fun getPlaygroundsbyName(sport:String): LiveData<List<String>> {
    fun loadPlaygrounds() {
        //val playgrounds = MutableLiveData<List<String>>()
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
        //return playgrounds
    }

    //fun getFullDates(playground: String): LiveData<List<Date>> {
    fun loadFullDates() {
        //val fullDates = MutableLiveData<List<Date>>()
        datesMap.clear()
        fullDates.clear()
        viewModelScope.launch(Dispatchers.IO) {
            reg = db.collection("Users/${Firebase.auth.uid}/Reservations")
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
            /*try {
                //db.collection("Reservation/${selectedPlayground.value}/")
                db.collection("Users/${Firebase.auth.uid}/Reservations")
                    //.whereEqualTo("playgroundName", playground)
                    .get()
                    .addOnSuccessListener { documents ->
                        val datesMap = mutableMapOf<Date, Int>()
                        for (res in documents) {
                            val reservation = res.toObject(Reservation::class.java)
                            val date = reservation.date // assuming 'date' field in Reservation class
                            datesMap[date] = datesMap.getOrDefault(date, 0) + 1
                        }
                        fullDates.clear()
                        fullDates.addAll(datesMap.filter { it.value >= 12 }.keys.toList())
                        //fullDates.postValue(fullDatesList)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }


            } catch (e: Exception) {
                Log.w(TAG, "Exception occurred = $e")
            }*/
        }

        //return fullDates
    }

    /*
    fun getRecyclerAdapter(fasce:List<FasciaOraria>,sport:String,field:String,date:Date,time:String):RentRecyclerViewAdapter{
        return RentRecyclerViewAdapter(fasce.map { x->it.polito.mad.lab5.reservation.ShowReservationModel(x.id,sport,field,date,time,x.oraInizio,x.oraFine, "") },date,sport,field,this);
    }*/

    fun saveReservation(/*reservation: Reservation*/) {
        val reservation = Reservation(
            discipline = selectedSport.value,
            playgroundName = selectedPlayground.value,
            date = selectedDate.value!!,
            oraInizio = selectedTimeSlot.value!!.oraInizio,
            oraFine = selectedTimeSlot.value!!.oraFine,
            customRequest = customRequest.value
        )
        /*
        val localDate: LocalDate = selectedDate.value!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val year: Int = localDate.year
        val month: Int = localDate.monthValue
        val day: Int = localDate.dayOfMonth
        */
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //db.collection("Reservations/${selectedPlayground.value}")
                db.collection("Users/${Firebase.auth.uid}/Reservations")
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

    override fun onCleared() {
        super.onCleared()
        reg.remove()
        regFullDates.remove()
    }
}