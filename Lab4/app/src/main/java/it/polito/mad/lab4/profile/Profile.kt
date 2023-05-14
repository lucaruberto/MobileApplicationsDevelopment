package it.polito.mad.lab4.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.mad.lab4.db.User
import it.polito.mad.lab4.db.Sports
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path.toString())
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
    editor.putString("sportlist", Json.encodeToString(userData.sportslist))
    editor.apply()
}
data class UserData(val fullName: String, val nickname: String,val mail: String,val birthdate: String,val sex: String
                    ,val city: String,val sportslist: List<SportList>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(checkpermission : ()->Unit, context: Context) {

    val viewModel: ProfileViewModel = viewModel()

    var user: User? =viewModel.getUserbyId(1).value;
    val (name,setName) = remember { mutableStateOf("") }
    val (nickname,setNickname) = remember { mutableStateOf("") }
    val (mail,setMail) = remember { mutableStateOf("") }
    val (birthdate,setBirthdate) = remember { mutableStateOf("") }
    val (sex,setSex) = remember { mutableStateOf("") }
    val (city,setCity) = remember { mutableStateOf("") }
    val (sport,setSport) = remember { mutableStateOf("") }
    val (editmode,setEditMode) = remember { mutableStateOf(false) }
    val (changephotoexpanded,setChangePhotoExpanded) = remember {
        mutableStateOf(false)
    }

    var (selectedSports,setSelectedSport) = remember { mutableStateOf(emptyList<Sports>()) }
    var (selectedSportsLevel,setSelectedSportLevel) = remember { mutableStateOf(emptyList<SportList>()) }
    var (showDialog,setShowDialog) = remember { mutableStateOf(false) }
    //valori di prova
    val sports= listOf<Sports>(Sports(1,"Calcio"), Sports(2,"Basket"), Sports(3,"Danza"))


    if(user != null)
    {
        setName(user.fullname)
        setNickname(user.nickname);
        setMail(user.mail);
        setBirthdate(user.birthdate);
        setSex(user.sex);
        setCity(user.city);
    }


    Scaffold(topBar = {

            myTopBar(editmode = editmode, setEditMode =setEditMode , u = user,viewModel,name,nickname,mail,birthdate,sex,city)

    }, content = {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                ProfileImage(
                    editmode,
                    changephotoexpanded,
                    setChangePhotoExpanded,
                    checkpermission,
                    context
                )

            }

            ProfileField(hover = "Nickname:", text = nickname , setText = setNickname, editmode =editmode )
            ProfileField(hover = "FullName:", text = name, setText = setName, editmode = editmode)
            ProfileField(hover= "Mail:",text =mail , setText = setMail , editmode = editmode)
            ProfileField(hover= "Birthdate:",text =birthdate , setText = setBirthdate , editmode = editmode)
            ProfileField(hover= "Sex:",text =sex , setText = setSex , editmode = editmode)
            ProfileField(hover= "City:",text =city , setText = setCity , editmode = editmode)
            ProfileField(hover= "Sport:",text =sport , setText = setSport , editmode = editmode)
            Button(onClick = {setShowDialog(true) }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Modifica Sport")
            }

            if(showDialog){
                SelectSportsDialog(
                    availableSports = sports,
                    selectedSports = selectedSports,
                    setSelectedSport = setSelectedSport,
                    onAddSport = { sport ->
                        // Do something when a sport is selected
                    },
                    onDismissRequest = {showDialog=false },
                    setShowDialog =setShowDialog,
                    selectedSportLevel = selectedSportsLevel,
                    setSelectedSportLevel = setSelectedSportLevel
                )
            }

        }

    })






}