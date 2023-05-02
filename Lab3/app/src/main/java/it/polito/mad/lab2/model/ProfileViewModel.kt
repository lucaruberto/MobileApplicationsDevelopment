package it.polito.mad.lab2.model

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import it.polito.mad.lab2.R
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileViewModel(private val state: SavedStateHandle): ViewModel() {
    private val _nickname = MutableLiveData<String>(state["nickname"] ?: "")
    val nickname: LiveData<String> = _nickname

    private val _fullName = MutableLiveData<String>(state["fullName"])
    val fullName: LiveData<String> = _fullName

    private val _email = MutableLiveData<String>(state["email"] ?: "")
    val email: LiveData<String> = _email

    private val _birth = MutableLiveData<String>(state["birth"] ?: "")
    val birth: LiveData<String> = _birth

    private val _gender = MutableLiveData<String>(state["gender"] ?: "")
    val gender: LiveData<String> = _gender

    private val _city = MutableLiveData<String>(state["city"] ?: "")
    val city: LiveData<String> = _city

    private val _sports = MutableLiveData<String>(state["sports"] ?: "")
    val sports: LiveData<String> = _sports

    private val _imageUri = MutableLiveData<Uri>(state["imageUri"] ?: null)
    val imageUri: LiveData<Uri> = _imageUri

    fun setNickname(newNickname: String) {
        _nickname.value = newNickname
        state["nickname"] = newNickname
    }

    fun setFullName(newFullName: String) {
        _fullName.value = newFullName
        state["fullName"] = newFullName
    }

    fun setEmail(newEmail: String) {
        _email.value = newEmail
        state["email"] = newEmail
    }

    fun setBirth(newBirth: String) {
        _birth.value = newBirth
        state["birth"] = newBirth
    }

    fun setGender(newGender: String) {
        _gender.value = newGender
        state["gender"] = newGender
    }

    fun setCity(newCity: String) {
        _city.value = newCity
        state["city"] = newCity
    }

    fun setSports(newSports: String) {
        _sports.value = newSports
        state["sports"] = newSports
    }

    fun setImageUri(newImageUri: Uri?) {
        if (newImageUri != null) {
            _imageUri.value = newImageUri!!
            state["imageUri"] = newImageUri
        }
    }
}