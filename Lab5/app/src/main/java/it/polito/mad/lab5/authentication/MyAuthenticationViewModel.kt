package it.polito.mad.lab5.authentication


import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MyAuthenticationViewModel(val startListenerRegistration: () -> Unit) : ViewModel() {
    private val auth = Firebase.auth

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
                    val authResult = auth.signInWithEmailAndPassword(email.value, password.value).await()
                    if(authResult.user != null){
                        Log.d(ContentValues.TAG, "User logged in as " + authResult.user!!.uid)
                        startListenerRegistration()
                        isLogged.value = true
                    }

                } catch (e: Exception) {
                    Log.w(ContentValues.TAG, "Error user login", e)
                }
            }
        }
    }
}