package com.example.firebasetest.ui


import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MyAuthenticationViewModel: ViewModel() {
    val auth = Firebase.auth

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val repeatPassword = mutableStateOf("")

    val isRegistered = mutableStateOf(true)
    val isLogged = mutableStateOf(false)

    val equalPasswords = mutableStateOf(0)

    fun firebaseSignUpWithEmailAndPassword(/*email: String, password: String*/) {
        viewModelScope.launch {
            if (email.value != "" && password.value != "") {
                try {
                    auth.createUserWithEmailAndPassword(email.value, password.value).await()
                    Log.d(ContentValues.TAG, "User registered")
                    isRegistered.value = true
                } catch (e: Exception) {
                    Log.w(ContentValues.TAG, "Error registering user", e)
                }
            }
        }
    }

    fun firebaseSignInWithEmailAndPassword(/*email: String, password: String*/) {
        viewModelScope.launch {
            if (email.value != "" && password.value != "") {
                try {
                    auth.signInWithEmailAndPassword(email.value, password.value).await()
                    Log.d(ContentValues.TAG, "User logged in")
                    isLogged.value = true
                } catch (e: Exception) {
                    Log.w(ContentValues.TAG, "Error user login", e)
                }
            }
        }
    }
}