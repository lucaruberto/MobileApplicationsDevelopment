package it.polito.mad.lab5.friends

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import it.polito.mad.lab5.db.Friend
import it.polito.mad.lab5.db.Pending
import it.polito.mad.lab5.db.ProvaUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

class FriendsViewModel(application: Application) : AndroidViewModel(application) {

    private var db = Firebase.firestore

    val editFriends = mutableStateOf(false)
    val friends_id : SnapshotStateList<Friend> = mutableStateListOf()
    val pending_id : SnapshotStateList<Pending> = mutableStateListOf()
    val searching_friends : SnapshotStateList<ProvaUser> = mutableStateListOf()
    val text = mutableStateOf("")
    fun fetchInitialData() {
        loadFriends()
        loadPending()
    }

    suspend fun getUserById(id : String, setUser: (ProvaUser) -> Unit) {
        val userDocRef = db.collection("Users").document(id)
        return try {
            val documentSnapshot = userDocRef.get().await()
            if (documentSnapshot.exists()) {
                val retrievedUser = documentSnapshot.toObject(ProvaUser::class.java)!!
                if(retrievedUser.imageUri != ""){
                    loadUserProfileImage(id, retrievedUser, setUser)
                    retrievedUser.imageUri = "Loading"
                }
                setUser(retrievedUser)
            } else {
                setUser(ProvaUser())
            }
        } catch (exception: Exception) {
            setUser(ProvaUser())
        }
    }

    private fun loadUserProfileImage(id: String, user: ProvaUser, setUser: (ProvaUser) -> Unit){
        val storageReference = FirebaseStorage.getInstance().getReference("profileImages/$id.jpg")
        val localProfileImageFile = File.createTempFile("localProfileImage", ".jpg")
        Log.d(ContentValues.TAG, "Start downloading Friend Profile Image")
        storageReference
            .getFile(localProfileImageFile)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Friend Profile Image downloaded successfully")
                val userWithImage = ProvaUser(
                    user.name,
                    user.nickname,
                    user.email,
                    user.birthdate,
                    user.sex,
                    user.city,
                    localProfileImageFile.toUri().toString()
                )
                Log.d(ContentValues.TAG, "Profile Image stored to ${userWithImage.imageUri}")
                setUser(userWithImage)
            }
            .addOnFailureListener { e: Exception ->
                Log.w(ContentValues.TAG, "Profile image download error: $e")
            }
    }
    suspend fun getUserbyNickname(nickname: String) : ProvaUser{
        val userDocRef = db.collection("Users").document(nickname)
        return try {
            val documentSnapshot = userDocRef.get().await()
            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(ProvaUser::class.java)!!
            } else {
                ProvaUser()
            }
        } catch (exception: Exception) {
            ProvaUser()
        }
    }
    private fun loadFriends(){
            viewModelScope.launch (){
                    friends_id.clear()
                    db.collection("Users/${Firebase.auth.uid}/Friends")
                       .addSnapshotListener { snapshots, e ->
                           if (e != null) {
                               Log.w(ContentValues.TAG, "listen:error", e)
                           }
                           if(snapshots != null) {
                               for (dc in snapshots.documentChanges) {
                                   when (dc.type) {
                                       DocumentChange.Type.ADDED -> {
                                           Log.d(ContentValues.TAG, "New Friend: ${dc.document.data}")
                                           friends_id.add(dc.document.toObject(Friend::class.java))
                                       }

                                       DocumentChange.Type.MODIFIED -> {
                                           val updatedRes = dc.document.toObject(Friend::class.java)
                                           friends_id.removeIf {
                                               it.id == updatedRes.id
                                           }
                                           friends_id.add(updatedRes)
                                           Log.d(ContentValues.TAG, "Modified Friend: ${dc.document.data}")
                                       }

                                       DocumentChange.Type.REMOVED -> {
                                           Log.d(ContentValues.TAG, "Removed Friend: ${dc.document.data}")
                                           val friendToDelete = dc.document.toObject(Friend::class.java)
                                           friends_id.removeIf {
                                                       it.id == friendToDelete.id
                                           }
                                       }
                                   }
                               }
                           }
                       }


            }

    }
    private fun loadPending(){
        viewModelScope.launch (){
            friends_id.clear()
            db.collection("Users/${Firebase.auth.uid}/Pending")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "listen:error", e)
                    }
                    if(snapshots != null) {
                        for (dc in snapshots.documentChanges) {
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    Log.d(ContentValues.TAG, "New Pending: ${dc.document.data}")
                                    pending_id.add(dc.document.toObject(Pending::class.java))
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    val updatedRes = dc.document.toObject(Pending::class.java)
                                    pending_id.removeIf {
                                        it.id == updatedRes.id
                                    }
                                    pending_id.add(updatedRes)
                                    Log.d(ContentValues.TAG, "Modified Pending: ${dc.document.data}")
                                }

                                DocumentChange.Type.REMOVED -> {
                                    Log.d(ContentValues.TAG, "Removed Pending: ${dc.document.data}")
                                    val pendingToDelete = dc.document.toObject(Pending::class.java)
                                    pending_id.removeIf {
                                        it.id == pendingToDelete.id
                                    }
                                }
                            }
                        }
                    }
                }


        }

    }
    fun searchFriend(ref:String){
       viewModelScope.launch {
           searching_friends.clear()
           db.collection("Users").whereEqualTo("nickname",ref)
               .addSnapshotListener { snapshots, e ->
                   if (e != null) {
                       Log.w(ContentValues.TAG, "listen:error", e)
                   }
                   if(snapshots != null) {
                       println("Lo snapshot non e' nullo!")
                       println("Il numero di documenti cambiati e' " + snapshots.documentChanges.size)
                       for (dc in snapshots.documentChanges) {
                           when (dc.type) {
                               DocumentChange.Type.ADDED -> {
                                   Log.d(ContentValues.TAG, "New User: ${dc.document.data}")
                                   searching_friends.add(dc.document.toObject(ProvaUser::class.java))
                               }

                               DocumentChange.Type.MODIFIED -> {
                                   val updatedRes = dc.document.toObject(ProvaUser::class.java)
                                   searching_friends.removeIf {
                                       it.nickname == updatedRes.nickname
                                   }
                                   searching_friends.add(updatedRes)
                                   Log.d(ContentValues.TAG, "Modified User: ${dc.document.data}")
                               }

                               DocumentChange.Type.REMOVED -> {
                                   Log.d(ContentValues.TAG, "Removed User: ${dc.document.data}")
                                   val userToDelete = dc.document.toObject(ProvaUser::class.java)
                                   searching_friends.removeIf {
                                       it.nickname == userToDelete.nickname
                                   }
                               }
                           }
                       }
                   }
               }
       }
   }
    fun addPending(id: ProvaUser){
        val myid= Firebase.auth.uid
        if(myid!= null) {
            viewModelScope.launch {
                db.collection("Users")
                    .whereEqualTo("birthdate", id.birthdate)
                    .whereEqualTo("city", id.city)
                    .whereEqualTo("email", id.email)
                    .whereEqualTo("imageUri", id.imageUri)
                    .whereEqualTo("name", id.name)
                    .whereEqualTo("nickname", id.nickname)
                    .whereEqualTo("sex", id.sex)
                    .get()
                    .addOnSuccessListener { userdocument ->
                        db.collection("Users/${Firebase.auth.uid}/Pending")
                            .add(Pending(id = userdocument.documents[0].id, state = "Sent"))
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG, "Friend Added for the main user")

                                db.collection("Users/${userdocument.documents[0].id}/Pending")
                                    .add(Pending(id =myid,state="Received"))
                                    .addOnSuccessListener {
                                        Log.d(ContentValues.TAG, "Friend Added for the other user")

                                    }
                                    .addOnFailureListener {
                                        Log.d(
                                            ContentValues.TAG,
                                            "Error adding the friend for the other user"
                                        )

                                    }
                            }
                            .addOnFailureListener {
                                Log.d(
                                    ContentValues.TAG,
                                    "Error Adding the friend for the main user"
                                )
                            }
                    }


            }
        }
    }
    fun deleteFriend(id:String){
        viewModelScope.launch {
            db.collection("Users/${Firebase.auth.uid}/Friends")
                .whereEqualTo("id",id).get()
                .addOnSuccessListener { friendDocument ->

                    db.collection("Users/${Firebase.auth.uid}/Friends")
                        .document(friendDocument.documents[0].id)
                        .delete()
                        .addOnSuccessListener {

                            Log.d(ContentValues.TAG, "Friend Removed for the main user")

                            db.collection("Users/${id}/Friends").whereEqualTo("id",Firebase.auth.uid).get()
                                .addOnSuccessListener {otherfrienddocument ->
                                    db.collection("Users/${id}/Friends").document(otherfrienddocument.documents[0].id).delete()
                                        .addOnSuccessListener {
                                            Log.d(ContentValues.TAG, "Friend Removed for the other user")

                                        }
                                        .addOnFailureListener {
                                            Log.d(ContentValues.TAG, "Error removing the friend for the other user")

                                        }
                                }
                                .addOnFailureListener{
                                    Log.d(ContentValues.TAG, "Error seeking  for the other user document")
                                }

                        }
                        .addOnFailureListener {

                            Log.d(ContentValues.TAG, "Error removing the friend for the main user")

                        }

                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG, "Error seeking for the friend of the main user")
                }
        }
    }
    fun deletePending(id:String){
        val myid= Firebase.auth.uid

        if(myid!=null){

            viewModelScope.launch {
                db.collection("Users/${Firebase.auth.uid}/Pending").whereEqualTo("id",id).get()
                    .addOnSuccessListener {
                        db.collection("Users/${Firebase.auth.uid}/Pending").document(it.documents[0].id).delete()
                            .addOnSuccessListener {
                            db.collection("Users/${id}/Pending").whereEqualTo("id",myid).get()
                                .addOnSuccessListener { it2->
                                    db.collection("Users/${id}/Pending").document(it2.documents[0].id).delete()
                                        .addOnSuccessListener {
                                            Log.d(ContentValues.TAG, "Deleted Friend for the main user and the secondary user")

                                        }
                                }
                            }
                    }
            }
        }
    }
    fun addFriend(id:String){
       val myid=Firebase.auth.uid
       if(myid!=null){
           viewModelScope.launch{
               db.collection("Users/${Firebase.auth.uid}/Friends").add(Friend(id=id))
                   .addOnSuccessListener {
                       db.collection("Users/${Firebase.auth.uid}/Pending").whereEqualTo("id",id).get()
                           .addOnSuccessListener {
                               db.collection("Users/${Firebase.auth.uid}/Pending").document(it.documents[0].id).delete()
                                   .addOnSuccessListener {
                                       Log.d(ContentValues.TAG, "Added Friend for the main user")

                                   }
                                   .addOnFailureListener {
                                       println("Errore nel cancellare la richiesta pending")
                                   }
                           }

                   }
               db.collection("Users/${id}/Friends").add(Friend(id=myid))
                   .addOnSuccessListener {
                      db.collection("Users/${id}/Pending").whereEqualTo("id",myid).get()
                          .addOnSuccessListener {
                              db.collection("Users/${id}/Pending").document(it.documents[0].id).delete()
                                  .addOnSuccessListener {
                                      Log.d(ContentValues.TAG, "Added Friend for the secondary user")

                                  }
                          }
                   }
           }

       }
   }



}