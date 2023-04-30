package it.polito.mad.lab2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.polito.mad.lab2.db.GlobalDatabase

class SearchPlaygroundViewModel(application: Application) : AndroidViewModel(application) {

    val db = GlobalDatabase.getDatabase(getApplication<Application>().applicationContext);


}