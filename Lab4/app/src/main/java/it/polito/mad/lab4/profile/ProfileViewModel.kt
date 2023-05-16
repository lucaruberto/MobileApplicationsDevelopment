package it.polito.mad.lab4.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import it.polito.mad.lab4.db.GlobalDatabase
import it.polito.mad.lab4.db.Sports

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val db = GlobalDatabase.getDatabase(application.applicationContext)

    fun getAllSports(): LiveData<List<Sports>> {
        return db.sportsDao().getSports()
    }
}