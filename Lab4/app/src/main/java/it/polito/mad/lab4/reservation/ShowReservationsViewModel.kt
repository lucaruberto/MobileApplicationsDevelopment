package it.polito.mad.lab4.reservation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import it.polito.mad.lab4.db.GlobalDatabase
import it.polito.mad.lab4.db.Reservation
import java.util.Date
import kotlin.concurrent.thread

class ShowReservationsViewModel(application: Application) : AndroidViewModel(application) {
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
}