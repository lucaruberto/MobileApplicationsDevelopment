@file:Suppress("DEPRECATION")

package it.polito.mad.lab5.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.mad.lab5.db.ProvaUser
import it.polito.mad.lab5.db.Sports
import java.io.ByteArrayOutputStream

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path.toString())
}


@Composable
fun Profile(context: Context,userid:String) {

    val viewModel: ProfileViewModel = viewModel()
    viewModel.fetchUser(userid)
    viewModel.fetchUserSports(userid)
    viewModel.fetchAllSports()
    val utente :ProvaUser?= viewModel.user.value
    val chosensport = viewModel.selectedsportlevel
    val allsports =  viewModel.allSports

    if (utente != null){

            val (name,setName) = remember { mutableStateOf(utente.FullName)}
            val (nickname,setNickname) = rememberSaveable { mutableStateOf(utente.Nickname)}
            val (mail,setMail) = rememberSaveable { mutableStateOf(utente.Mail)}
            val (birthdate,setBirthdate) = rememberSaveable { mutableStateOf(utente.Birthdate)}
            val (sex,setSex) = rememberSaveable { mutableStateOf(utente.Sex)}
            val (city,setCity) = rememberSaveable { mutableStateOf(utente.City)}
            val (imageUri,setImageUri) = rememberSaveable { mutableStateOf(utente.imageUri)}
            val (editmode,setEditMode) = rememberSaveable { mutableStateOf(false)}
            val (changephotoexpanded,setChangePhotoExpanded) = rememberSaveable { mutableStateOf(false)}
            val valsStringdb = chosensport.map { Sports(discipline = it.SportName) }
            val selectedSports = remember { mutableStateListOf(*valsStringdb.toTypedArray()) }
            val selectedSportsLevel = remember { mutableStateListOf(*chosensport.toTypedArray())}
            var (showDialog,setShowDialog) = rememberSaveable { mutableStateOf(false) }



            Scaffold(
                topBar = {
                    MyTopBar(
                        userid =userid,
                        editmode = editmode,
                        setEditMode = setEditMode ,
                        viewModel = viewModel,
                        name = name,
                        nickname = nickname,
                        mail = mail,
                        birthdate = birthdate,
                        sex = sex,
                        city = city,
                        imageUri = imageUri,
                        selectedSportLevel = selectedSportsLevel
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


                        Row {
                            Column( modifier = if(editmode) Modifier.weight(0.7f) else Modifier.weight(1f) ) {
                                SportsTable(selectedSportsLevel,setShowDialog, editmode)
                            }


                        }
                        if(showDialog){
                            SelectSportsDialog(
                                availableSports = allsports,
                                selectedSports = selectedSports,
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