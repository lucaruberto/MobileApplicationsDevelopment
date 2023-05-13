package it.polito.mad.lab4.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import it.polito.mad.lab4.db.GlobalDatabase
import it.polito.mad.lab4.db.Reservation
import it.polito.mad.lab4.db.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.concurrent.thread

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val db = GlobalDatabase.getDatabase(application.applicationContext)


    fun getUserbyId(i: Int): LiveData<User> {
       return db.userDao().getUserbyId(i);
    }

    fun saveUpdatedUser(u: User){
        return db.userDao().update(u);
    }

    fun saveReservation(id: Int,date: Date,time:String,discipline:String,starthour:Int,endHour:Int,field: String, customRequest: String){
        thread { db.reservationDao().save(Reservation(id,date,time,discipline,starthour,endHour,field, customRequest)) };
        return
    }

    fun insertUser(u: User){
        viewModelScope.launch(Dispatchers.IO){
            db.userDao().save(u)
        }
    }

}