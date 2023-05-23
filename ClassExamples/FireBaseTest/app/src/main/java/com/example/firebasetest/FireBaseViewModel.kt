package com.example.firebasetest

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireBaseViewModel: ViewModel(){
    private val db = Firebase.firestore

    val users = mutableStateListOf<String>()

    private val reg: ListenerRegistration = db
        .collection("users")
        .addSnapshotListener {
                value, error ->
            if(error != null) Log.w(TAG, "Error getting documents.", error)
            if(value != null) {
                for (change in value.documentChanges) {
                    val document = change.document
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            users.add("${document.id} => ${document.data}")
                        }
                        DocumentChange.Type.REMOVED -> {
                            users.remove("${document.id} => ${document.data}")
                        }
                        else -> {
                            // ChangeType == MODIFIED
                        }
                    }
                }
            }
        }

    fun addUsers(){
        // Create a new user with a first and last name
        var user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815,
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

    fun readUsers() {
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

    override fun onCleared() {
        super.onCleared()
        reg.remove()
    }
}