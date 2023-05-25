package it.polito.mad.lab5.profile

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.mad.lab5.db.User
import it.polito.mad.lab5.db.GlobalDatabase
import it.polito.mad.lab5.db.ProvaSports
import it.polito.mad.lab5.db.ProvaUser
import it.polito.mad.lab5.db.ProvaUserSports
import it.polito.mad.lab5.db.Sports
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.log


class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val db = GlobalDatabase.getDatabase(application.applicationContext)
    var dbreal = FirebaseFirestore.getInstance()
    var user: MutableState<ProvaUser?> = mutableStateOf(null)
        private set

    var selectedsportlevel : MutableList<ProvaUserSports> = mutableListOf()
    var allSports : MutableList<ProvaSports> = mutableListOf()

    fun fetchUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocument =
                    dbreal.collection("Users").document(userId).get().await()
                user.value = userDocument.toObject(ProvaUser::class.java)

            } catch (e: Exception) {
                println("Exception occurred = " + e.toString())
            }
        }
    }
    fun fetchUserSports(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userSportDocument =
                    dbreal.collection("Users/"+userId+"/Sports").get().await()
                selectedsportlevel.clear()

                for (document in userSportDocument){
                    val sport = document.toObject(ProvaUserSports::class.java)
                    selectedsportlevel.add(sport);
                }
            } catch (e: Exception) {
                println("Exception occurred = " + e.toString())
            }
        }
    }
    fun fetchAllSports() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val SportDocuments = dbreal.collection("Sport").get().await()
                    allSports.clear()
                for (document in SportDocuments){
                    val sport = document.toObject(ProvaSports::class.java)
                    allSports.add(sport);
                }
            } catch (e: Exception) {
                println("Exception occurred = " + e.toString())
            }
        }
    }
    fun updateUser(userId: String, updatedUser: ProvaUser) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocument = dbreal.collection("Users").document(userId)
                userDocument.update(
                    "Birthdate", updatedUser.Birthdate,
                    "City", updatedUser.City,
                    "FullName", updatedUser.FullName,
                    "Nickname", updatedUser.Nickname,
                    "Mail", updatedUser.Mail,
                    "Sex", updatedUser.Sex,
                    "imageUri", updatedUser.imageUri
                ).await()

                val updatedUserDocument = userDocument.get().await()
                val updatedUser = updatedUserDocument.toObject(ProvaUser::class.java)

                user.value = updatedUser
            } catch (e: Exception) {
                println("Exception occurred = " + e.toString())
            }
        }
    }

    fun getAllSports(): LiveData<List<Sports>> {
        return db.sportsDao().getSports()
    }
}