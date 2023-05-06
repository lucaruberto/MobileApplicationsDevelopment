package it.polito.mad.lab3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import it.polito.mad.lab3.db.FasciaOraria
import it.polito.mad.lab3.db.GlobalDatabase
import it.polito.mad.lab3.db.Reservation
import java.util.Date
import kotlin.concurrent.thread

class RentViewModel(application: Application) : AndroidViewModel(application) {

    val db = GlobalDatabase.getDatabase(application.applicationContext)
    private var liveFasceLibere = db.fasciaorariaDao().getAllFasciaOraria()

    fun getFasceOrariLibere(playground: String, date: Date): LiveData<List<FasciaOraria>>{
        liveFasceLibere = db.fasciaorariaDao().getFreeSlots(playground, date)
        return liveFasceLibere
    }

    val sportsList: LiveData<List<String>> = db.sportsDao().getAll()

    fun getPlaygroundsbyName(sport:String):LiveData<List<String>>{
        return db.playgroundsDao().getPlayGroundsbySportName(sport)
    }
    /*
    fun getRecyclerAdapter(fasce:List<FasciaOraria>,sport:String,field:String,date:Date,time:String):RentRecyclerViewAdapter{
        return RentRecyclerViewAdapter(fasce.map { x->it.polito.mad.lab3.ShowReservationModel(x.id,sport,field,date,time,x.oraInizio,x.oraFine, "") },date,sport,field,this);
    }*/

    fun saveReservation(id: Int,date: Date,time:String,discipline:String,starthour:Int,endHour:Int,field: String, customRequest: String){
        thread { db.reservationDao().save(Reservation(id,date,time,discipline,starthour,endHour,field, customRequest)) };
        return
    }

}