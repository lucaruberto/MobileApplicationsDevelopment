package it.polito.mad.lab5.rent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import it.polito.mad.lab5.db.FasciaOraria
import it.polito.mad.lab5.db.GlobalDatabase
import it.polito.mad.lab5.db.Reservation
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

    fun getFullDates(playground: String): LiveData<List<Date>>{
        return db.reservationDao().getFullDates(playground)
    }
    /*
    fun getRecyclerAdapter(fasce:List<FasciaOraria>,sport:String,field:String,date:Date,time:String):RentRecyclerViewAdapter{
        return RentRecyclerViewAdapter(fasce.map { x->it.polito.mad.lab5.reservation.ShowReservationModel(x.id,sport,field,date,time,x.oraInizio,x.oraFine, "") },date,sport,field,this);
    }*/

    fun saveReservation(id: Int,date: Date,time:String,discipline:String,starthour:Int,endHour:Int,field: String, customRequest: String){
        thread { db.reservationDao().save(Reservation(id,date,time,discipline,starthour,endHour,field, customRequest)) };
        return
    }

}