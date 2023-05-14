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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.mad.lab4.db.Sports
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
            // editor.putString("sportlist", Json.encodeToString(userData.sportslist))
    editor.apply()
}
data class UserData(val fullName: String, val nickname: String,val mail: String,val birthdate: String,val sex: String
                    ,val city: String,val sportslist: List<SportList>)
{
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(checkpermission: () -> Unit, context: Context, user: UserData) {

    val viewModel: ProfileViewModel = viewModel()

    val (name,setName) = rememberSaveable { mutableStateOf(user.fullName) }
    val (nickname,setNickname) = rememberSaveable { mutableStateOf(user.nickname) }
    val (mail,setMail) = rememberSaveable { mutableStateOf(user.mail) }
    val (birthdate,setBirthdate) = rememberSaveable { mutableStateOf(user.birthdate) }
    val (sex,setSex) = rememberSaveable { mutableStateOf(user.sex) }
    val (city,setCity) = rememberSaveable { mutableStateOf(user.city) }
    val (sport,setSport) = rememberSaveable { mutableStateOf("") }
    val (editmode,setEditMode) = rememberSaveable { mutableStateOf(false) }
    val (changephotoexpanded,setChangePhotoExpanded) = rememberSaveable {
        mutableStateOf(false)
    }


    val selectedSports = remember { mutableStateListOf<Sports>() }
    val selectedSportsLevel = remember { mutableStateListOf<SportList>() }
    var (showDialog,setShowDialog) = rememberSaveable { mutableStateOf(false) }
    //valori di prova
   var user = remember {
        mutableStateOf<UserData>(UserData(name, nickname ,mail,birthdate,sex,city, emptyList()))
    }
    val sports= listOf<Sports>(Sports(1,"Calcio"), Sports(2,"Basket"), Sports(3,"Danza"))





    Scaffold(topBar = {

            myTopBar(editmode = editmode, setEditMode =setEditMode ,viewModel,name,nickname,mail,birthdate,sex,city, saveUserData = ::saveUserData, context = context, user = user.value)

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
            SportsTable()
            if(editmode){

                    ProfileField(hover= "Sport:",text =sport , setText = setSport , editmode = editmode)

                    Button(onClick = {setShowDialog(true) }, modifier = Modifier.width(250.dp)) {
                        Text(text = "Edit Sport")
                    }


            }
            else
                ProfileField(hover= "Sport:",text =sport , setText = setSport , editmode = editmode)



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

    })






}