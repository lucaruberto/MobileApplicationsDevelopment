package com.example.firebasetest

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import java.time.LocalDate
import java.time.LocalDateTime

class FireBaseViewModel: ViewModel(){
    private val db = Firebase.firestore

    val texts = mutableStateListOf<String>()

    val text = mutableStateOf("")

    /*private val reg: ListenerRegistration = db
        .collection("users")
        .document("${Firebase.auth.uid}")
        .addSnapshotListener {
                value, error ->
            if(Firebase.auth.uid != null) {
                if (error != null)
                    Log.w(TAG, "Error getting documents.", error)
                if (value != null) {
                    for (doc in value.documents){
                        users.add("${doc.id} => ${doc.data}")
                    }
                    /*for (change in value.documentChanges) {
                        val document = change.document
                        when (change.type) {
                            DocumentChange.Type.ADDED -> {
                                users.add("${document.id} => ${document.data}")
                            }

                            DocumentChange.Type.REMOVED -> {
                                users.remove("${document.id} => ${document.data}")
                            }

                            DocumentChange.Type.MODIFIED -> {
                                users.removeIf() { it.startsWith(document.id) }
                                users.add("${document.id} => ${document.data}")
                            }

                            else -> {
                                // ChangeType == MODIFIED
                            }
                        }
                    }*/
                }
            }
        }*/
    private lateinit var reg: ListenerRegistration

    fun startListenerRegistration() {
        Log.d(TAG, "Start listening on uid: ${Firebase.auth.uid}")
        reg = db
            .collection("texts")
            //.whereEqualTo("uid", "${Firebase.auth.uid}")
            .document(Firebase.auth.uid!!)
            .addSnapshotListener { document, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    //return@addSnapshotListener
                }
                if(document != null) {
                    /*for (dc in snapshots.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> Log.d(TAG, "New user: ${dc.document.data}")
                            DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified user: ${dc.document.data}")
                            DocumentChange.Type.REMOVED -> Log.d(TAG, "Removed user: ${dc.document.data}")
                        }
                    }*/
                    texts.clear()
                    texts.add("${document.id} => ${document.data}")
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addText(){
        db.collection("texts")
            .document(Firebase.auth.uid!!)
            .get()
            .addOnSuccessListener {document ->
                if (document.exists()) {
                    // document exists and only have to be updated
                    db.collection("texts")
                        //.add(user)
                        .document(Firebase.auth.uid!!)
                        //.set(textHash)
                        .update("text", FieldValue.arrayUnion(text.value))
                        //.set(textHash, SetOptions.merge())
                        .addOnSuccessListener { /*documentReference ->*/
                            //Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                            Log.d(ContentValues.TAG, "Text added")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding text", e)
                        }
                } else {
                    val textHash = hashMapOf(
                        "text" to listOf(text.value)
                    )

                    // document does not exist so must be created
                    db.collection("texts")
                        //.add(user)
                        .document(Firebase.auth.uid!!)
                        .set(textHash)
                        //.update("text", FieldValue.arrayUnion(text.value))
                        //.set(textHash, SetOptions.merge())
                        .addOnSuccessListener { /*documentReference ->*/
                            //Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                            Log.d(ContentValues.TAG, "Text created")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error creating text", e)
                        }
                }

            }
        // Add a new document with the user ID


        /*
        // Create a new user with a first, middle, and last name
        user = hashMapOf(
            "first" to "Alan",
            "middle" to "Mathison",
            "last" to "Turing",
            "born" to 1912,
        )

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

         */
    }

    /*fun readUsers() {
        // read from database
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    users.add("${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

     */

    override fun onCleared() {
        super.onCleared()
        reg.remove()
    }
}