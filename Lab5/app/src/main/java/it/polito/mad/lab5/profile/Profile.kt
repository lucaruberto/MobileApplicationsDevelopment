package it.polito.mad.lab5.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import it.polito.mad.lab5.db.ProvaSports
import it.polito.mad.lab5.db.ProvaUser
import it.polito.mad.lab5.db.Sports
import java.io.ByteArrayOutputStream

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path.toString())
}


data class UserData(val fullName: String, val nickname: String,val mail: String,val birthdate: String,val sex: String
                    ,val city: String,val selectedSportsLevel: String,val imageUri : String
)
{
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(context: Context, user: UserData) {

    val viewModel: ProfileViewModel = viewModel()
    val gson = Gson()
    viewModel.fetchUser("rsL1mFSgvJOkjmMUeHXb");
    viewModel.fetchUserSports("rsL1mFSgvJOkjmMUeHXb")
    viewModel.fetchAllSports()
    val utente :ProvaUser?= viewModel.user.value
    val chosensport = viewModel.selectedsportlevel
    val allsports =  viewModel.allSports

    if (utente != null && chosensport!= null && allsports != null ){

            val (name,setName) = remember { mutableStateOf(utente.FullName) }
            val (nickname,setNickname) = rememberSaveable { mutableStateOf(utente.Nickname) }
            val (mail,setMail) = rememberSaveable { mutableStateOf(utente.Mail) }
            val (birthdate,setBirthdate) = rememberSaveable { mutableStateOf(utente.Birthdate) }
            val (sex,setSex) = rememberSaveable { mutableStateOf(utente.Sex) }
            val (city,setCity) = rememberSaveable { mutableStateOf(utente.City) }
            val (imageUri,setImageUri) = rememberSaveable { mutableStateOf(utente.imageUri) }

            val (editmode,setEditMode) = rememberSaveable { mutableStateOf(false) }
            val (changephotoexpanded,setChangePhotoExpanded) = rememberSaveable {
                mutableStateOf(false)
            }


            val fromJson = gson.fromJson(user.selectedSportsLevel, Array<SportList>::class.java)
            val vals = fromJson?.toMutableList() ?: mutableListOf()
            val valsString =vals.map { ProvaSports(discipline = it.sportname) }
            val valsStringdb = chosensport.map { Sports(discipline = it.SportName) }
            val selectedSports = remember { mutableStateListOf(*valsStringdb.toTypedArray()) }

            val selectedSportsLevel = remember {
                mutableStateListOf(*chosensport.toTypedArray())
            }
     
            var (showDialog,setShowDialog) = rememberSaveable { mutableStateOf(false) }

            val user = remember {
                mutableStateOf(UserData(name, nickname ,mail, birthdate, sex, city, gson.toJson(selectedSportsLevel.toList()), imageUri))
            }

            Scaffold(
                topBar = {
                    MyTopBar(
                        userid="rsL1mFSgvJOkjmMUeHXb",
                        editmode = editmode,
                        setEditMode = setEditMode ,
                        viewModel = viewModel,
                        name = name,
                        nickname = nickname,
                        mail = mail,
                        birthdate = birthdate,
                        sex = sex,
                        city = city,
                        user = user.value,
                        context = context,
                        imageUri = imageUri,
                        selectedSportLevel = gson.toJson(selectedSportsLevel)
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
                                type = "simple",
                                editmode = editmode
                            )
                            ProfileField(
                                hover = "FullName",
                                text = name,
                                setText = setName,
                                type = "simple",
                                editmode = editmode
                            )
                            ProfileField(
                                hover = "Mail",
                                text = mail,
                                setText = setMail,
                                type = "mail",
                                editmode = editmode
                            )
                            ProfileField(
                                hover = "Birthdate",
                                text = birthdate,
                                setText = setBirthdate,
                                type = "date",
                                editmode = editmode
                            )
                            ProfileField(
                                hover = "Sex",
                                text = sex,
                                setText = setSex,
                                type = "simple",
                                editmode = editmode
                            )
                            ProfileField(
                                hover = "City",
                                text = city,
                                setText = setCity,
                                type = "simple-last",
                                editmode = editmode
                            )
                        }

                        //
                        Row {
                            Column( modifier = if(editmode) Modifier.weight(0.7f) else Modifier.weight(1f) ) {
                                SportsTable(selectedSportsLevel,setShowDialog, editmode)
                            }


                        }
                        if(showDialog){
                            SelectSportsDialog(
                                availableSports = allsports,
                                selectedSports = selectedSports,
                                onAddSport = { sport ->
                                    // Do something when a sport is selected
                                },
                                onDismissRequest = {showDialog=false },
                                setShowDialog =setShowDialog,
                                selectedSportLevel = selectedSportsLevel

                            )
                        }

                    }
                }
            )
        }
    }