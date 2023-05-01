package it.polito.mad.lab2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import it.polito.mad.lab2.db.FasciaOraria
import it.polito.mad.lab2.db.GlobalDatabase
import it.polito.mad.lab2.db.Reservation
import java.util.Date
import kotlin.concurrent.thread

class SearchPlaygroundViewModel(application: Application) : AndroidViewModel(application) {

    val db = GlobalDatabase.getDatabase(getApplication<Application>().applicationContext);


    fun getFasceOrari():LiveData<List<FasciaOraria>>{

        return db.fasciaorariaDao().getAllFasciaOraria()
    }
    fun getListSport():LiveData<List<String>>{

        return db.sportsDao().getAll()
    }

    fun getPlaygroundsbyName(sport:String):LiveData<List<String>>{
        return db.playgroundsDao().getPlayGroundsbySportName(sport)
    }

    fun getRecyclerAdapter(fasce:List<FasciaOraria>,sport:String,field:String,date:Date,time:String):Playground_RecyclerViewAdapter{
        return Playground_RecyclerViewAdapter(fasce.map { x->it.polito.mad.lab2.ShowReservationModel(x.id,sport,field,date,time,x.oraInizio,x.oraFine,"") },date,sport,field,this);
    }

    fun saveReservation(id: Int,date: Date,time:String,discipline:String,starthour:Int,endHour:Int,field: String, customRequest: String){
        thread { db.reservationDao().save(Reservation(id,date,time,discipline,starthour,endHour,field, customRequest)) };
        return
    }

}