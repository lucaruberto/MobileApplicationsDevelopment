package it.polito.mad.lab2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import it.polito.mad.lab2.db.FasciaOraria
import it.polito.mad.lab2.db.GlobalDatabase
import it.polito.mad.lab2.db.Reservation
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.util.Date
import kotlin.concurrent.thread

class SearchPlaygroundViewModel(application: Application) : AndroidViewModel(application) {

    val db = GlobalDatabase.getDatabase(getApplication<Application>().applicationContext)
    private var liveFasceLibere = db.fasciaorariaDao().getAllFasciaOraria()

    fun getFasceOrari():LiveData<List<FasciaOraria>>{
        return db.fasciaorariaDao().getAllFasciaOraria()
    }
    fun getFasceOrariLibere(playground: String, date: Date): LiveData<List<FasciaOraria>>{
        //test hardcoded
        //val p = "Ruffini"
        //val d = SimpleDateFormat("dd-MM-yyyy").parse("29-05-2023")
        //liveFasceLibere = db.fasciaorariaDao().getFreeSlots(p,d!!)

        //PROBLEMA: nel db c'è una prenotazione di FootBall al Ruffini per il giorno 29-05, fascia oraria 9-10
        // Ho settato p e d con valori hardcoded che dovrebbero corrispondere e far tornare correttamente la query
        // ma ciò non accade: ritorna sempre NULL anche con valori che so essere presenti!
        // (testando su DB Explorer invece la query ritorna quello che mi aspetto)

        //chiamata "vera"
        liveFasceLibere = db.fasciaorariaDao().getFreeSlots(playground, date)
        return liveFasceLibere
    }
    fun getListSport():LiveData<List<String>>{
        return db.sportsDao().getAll()
    }

    fun getPlaygroundsbyName(sport:String):LiveData<List<String>>{
        return db.playgroundsDao().getPlayGroundsbySportName(sport)
    }

    fun getRecyclerAdapter(fasce:List<FasciaOraria>,sport:String,field:String,date:Date,time:String):Playground_RecyclerViewAdapter{
        return Playground_RecyclerViewAdapter(fasce.map { x->it.polito.mad.lab2.ShowReservationModel(x.id,sport,field,date,time,x.oraInizio,x.oraFine, "") },date,sport,field,this);
    }

    fun saveReservation(id: Int,date: Date,time:String,discipline:String,starthour:Int,endHour:Int,field: String, customRequest: String){
        thread { db.reservationDao().save(Reservation(id,date,time,discipline,starthour,endHour,field, customRequest)) };
        return
    }

}