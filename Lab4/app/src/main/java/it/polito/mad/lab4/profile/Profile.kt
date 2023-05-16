package it.polito.mad.lab4.profile

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import it.polito.mad.lab4.db.Sports
import java.io.ByteArrayOutputStream

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path.toString())
}

fun getUserDataFromSP(context: Context): UserData {
    val sharedPref = context.getSharedPreferences("UserData", MODE_PRIVATE)

    val nicknameSP = sharedPref.getString("nickname", "") ?: ""
    val fullnameSP = sharedPref.getString("fullname", "") ?: ""
    val emailSP = sharedPref.getString("mail", "") ?: ""
    val birthSP = sharedPref.getString("birthdate", "") ?: ""
    val sexSP = sharedPref.getString("sex", "") ?: ""
    val citySP = sharedPref.getString("city", "") ?: ""
    val imageUriSP = sharedPref.getString("imageUri","") ?: ""
    val sportlistSP = sharedPref.getString("sportlist","") ?: ""

    return UserData(fullnameSP,nicknameSP,emailSP,birthSP,sexSP,citySP, sportlistSP,imageUriSP)
}
fun saveUserData(userData: UserData, context: Context) {
    val sharedPref = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putString("fullname", userData.fullName)
    editor.putString("nickname", userData.nickname)
    editor.putString("mail", userData.mail)
    editor.putString("birthdate", userData.birthdate)
    editor.putString("sex", userData.sex)
    editor.putString("city", userData.city)
    editor.putString("imageUri",userData.imageUri)
    editor.putString("sportlist", userData.selectedSportsLevel)
    editor.apply()
}
data class UserData(val fullName: String, val nickname: String,val mail: String,val birthdate: String,val sex: String
                    ,val city: String,val selectedSportsLevel: String,val imageUri : String
)
{
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(context: Context) {

    val viewModel: ProfileViewModel = viewModel()
    val gson = Gson()

    val userFromSP = getUserDataFromSP(context)

    val (name,setName) = remember { mutableStateOf(userFromSP.fullName) }
    val (nickname,setNickname) = rememberSaveable { mutableStateOf(userFromSP.nickname) }
    val (mail,setMail) = rememberSaveable { mutableStateOf(userFromSP.mail) }
    val (birthdate,setBirthdate) = rememberSaveable { mutableStateOf(userFromSP.birthdate) }
    val (sex,setSex) = rememberSaveable { mutableStateOf(userFromSP.sex) }
    val (city,setCity) = rememberSaveable { mutableStateOf(userFromSP.city) }
    val (imageUri,setImageUri) = rememberSaveable { mutableStateOf(userFromSP.imageUri) }

    val (editmode,setEditMode) = rememberSaveable { mutableStateOf(false) }
    val (changephotoexpanded,setChangePhotoExpanded) = rememberSaveable {
        mutableStateOf(false)
    }


    val fromJson = gson.fromJson(userFromSP.selectedSportsLevel, Array<SportList>::class.java)
    val vals = fromJson?.toMutableList() ?: mutableListOf()
    val valsString =vals.map { Sports(discipline = it.sportname) }
    val selectedSports = remember { mutableStateListOf(*valsString.toTypedArray()) }

    val selectedSportsLevel = remember { mutableStateListOf(*vals.toTypedArray()) }
    var (showDialog,setShowDialog) = rememberSaveable { mutableStateOf(false) }

    //valori di prova
    val user = remember {
        mutableStateOf(UserData(name, nickname ,mail, birthdate, sex, city, gson.toJson(selectedSportsLevel.toList()), imageUri))
    }
    val sports by viewModel.getAllSports().observeAsState(initial = emptyList())


    Scaffold(
        topBar = {
            MyTopBar(editmode, setEditMode ,viewModel,name,nickname,mail,birthdate,sex,city,
                ::saveUserData, user.value, context,
                imageUri, gson.toJson(selectedSportsLevel)
            )
        },
        content = {
            Column(modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    ProfileImage(
                        imageUri,
                        setImageUri,
                        editmode,
                        changephotoexpanded,
                        setChangePhotoExpanded,
                        context
                    )
                }

                Column(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()) {

                    ProfileField(
                        hover = "Nickname",
                        text = nickname,
                        setText = setNickname,
                        editmode = editmode
                    )
                    ProfileField(
                        hover = "FullName",
                        text = name,
                        setText = setName,
                        editmode = editmode
                    )
                    ProfileField(
                        hover = "Mail",
                        text = mail,
                        setText = setMail,
                        editmode = editmode
                    )
                    ProfileField(
                        hover = "Birthdate",
                        text = birthdate,
                        setText = setBirthdate,
                        editmode = editmode
                    )
                    ProfileField(
                        hover = "Sex",
                        text = sex,
                        setText = setSex,
                        editmode = editmode
                    )
                    ProfileField(
                        hover = "City",
                        text = city,
                        setText = setCity,
                        editmode = editmode
                    )
                }

                //
                Row {
                    Column( modifier = if(editmode) Modifier.weight(0.7f) else Modifier.weight(1f) ) {
                        SportsTable(selectedSportsLevel, setShowDialog)
                    }



                }
                if(showDialog){
                    SelectSportsDialog(
                        availableSports = sports,
                        selectedSports = selectedSports,
                        onAddSport = { sport ->
                            // Do something when a sport is selected
                        },
                        onDismissRequest = {showDialog=false },
                        setShowDialog =setShowDialog,
                        selectedSportLevel = selectedSportsLevel,
                    )
                }

            }
        }
    )
}