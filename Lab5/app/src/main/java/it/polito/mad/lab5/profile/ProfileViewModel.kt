package it.polito.mad.lab5.profile

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import it.polito.mad.lab5.db.ProvaSports
import it.polito.mad.lab5.db.ProvaUser
import it.polito.mad.lab5.db.ProvaUserSports
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var dbreal = FirebaseFirestore.getInstance()
    var user: MutableState<ProvaUser?> = mutableStateOf(null)
        private set

    var selectedsportlevel : MutableList<ProvaUserSports> = mutableListOf()
    var allSports : MutableList<ProvaSports> = mutableListOf()



    fun fetchUser(/*userId: String*/) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocument =
                    dbreal.collection("Users").document(Firebase.auth.uid!!).get().await()
                user.value = userDocument.toObject(ProvaUser::class.java)

            } catch (e: Exception) {
                println("Exception occurred = $e")
            }
        }
    }
    fun fetchUserSports(/*userId: String*/) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userSportDocument =
                    dbreal.collection("Users/${Firebase.auth.uid}/Sports").get().await()
                selectedsportlevel.clear()

                for (document in userSportDocument){
                    val sport = document.toObject(ProvaUserSports::class.java)
                    selectedsportlevel.add(sport)
                }
            } catch (e: Exception) {
                println("Exception occurred = $e")
            }
        }
    }
    fun fetchAllSports() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val sportDocuments = dbreal.collection("Sport").get().await()
                allSports.clear()
                for (document in sportDocuments){
                    val sport = document.toObject(ProvaSports::class.java)
                    allSports.add(sport)
                }
            } catch (e: Exception) {
                println("Exception occurred = $e")
            }
        }
    }
    fun updateUser(/*userId: String, */updatedUser: ProvaUser) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocument = dbreal.collection("Users").document(Firebase.auth.uid!!)
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
                val newUser = updatedUserDocument.toObject(ProvaUser::class.java)

                user.value = newUser
            } catch (e: Exception) {
                println("Exception occurred = $e")
            }
        }
    }
    fun updateUserSports(/*userId: String, */updateSports: SnapshotStateList<ProvaUserSports>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val batch = dbreal.batch()

                val userSports = dbreal.collection("Users/${Firebase.auth.uid}/Sports")

                 userSports.get()
                    .addOnSuccessListener { querySnapshot ->

                        for (document in querySnapshot) {
                            batch.delete(document.reference)
                        }
                        for(UserSport in updateSports.toList())
                        {
                            val id = userSports.document()
                            val newDocument = hashMapOf(
                                "SportName" to UserSport.SportName,
                                "Level" to UserSport.Level,
                            )
                            batch.set(id,newDocument)
                        }

                        batch.commit()
                            .addOnSuccessListener {
                                println("Documenti Modificati con successo.")
                            }
                            .addOnFailureListener { e ->
                                println("Si è verificato un errore durante l'eliminazione dei documenti: ${e.message}")
                            }
                    }
                    .addOnFailureListener { e ->
                        println("Si è verificato un errore durante il recupero dei documenti: ${e.message}")
                    }


            } catch (e: Exception) {
                println("Exception occurred = $e")
            }
        }
    }

}