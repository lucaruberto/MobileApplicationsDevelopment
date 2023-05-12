package it.polito.mad.lab4.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import it.polito.mad.lab4.db.User
import it.polito.mad.lab4.R
import java.io.ByteArrayOutputStream

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path.toString())
}
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

    if(user != null)
    {
        setName(user.fullname);
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
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                ProfileImage(editmode,changephotoexpanded,setChangePhotoExpanded,checkpermission,context)

            }

            ProfileField(hover = "Nickname", text = nickname , setText = setNickname, editmode =editmode )
            ProfileField(hover = "FullName", text = name, setText = setName, editmode = editmode)
            ProfileField(hover= "Mail",text =mail , setText = setMail , editmode = editmode)
            ProfileField(hover= "Birthdate",text =birthdate , setText = setBirthdate , editmode = editmode)
            ProfileField(hover= "Sex",text =sex , setText = setSex , editmode = editmode)
            ProfileField(hover= "City",text =city , setText = setCity , editmode = editmode)
            ProfileField(hover= "Sport",text =sport , setText = setSport , editmode = editmode)




        }

    })






}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileField(hover:String,text:String,setText : (String)->Unit,editmode: Boolean)
{
    Row(modifier = Modifier.padding(vertical = 5.dp), horizontalArrangement = Arrangement.SpaceEvenly)
    {


        Text(
            text = hover, modifier = Modifier
                .fillMaxHeight()
                .background(Color.Red, RoundedCornerShape(30.dp)), maxLines = 1
        )
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp), elevation = CardDefaults.cardElevation(10.dp)) {
            if (editmode)

                TextField(
                    value = text,
                    onValueChange = { setText(it) },
                    maxLines = 1,
                    singleLine = true
                )
            else
                Text(text = text)



        }
    }


}




@Composable
fun ProfileImage(editmode : Boolean,changephotoexpanded :  Boolean,setChangePhotoExpanded : (Boolean)->Unit,checkpermission : ()->Unit,context: Context){
    val imageUri = rememberSaveable { mutableStateOf("") }

    val photo=    rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()){
        if (it != null) {
           val x= getImageUriFromBitmap(context,it)
            imageUri.value= x.toString();

        }
    }

    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.baseline_person_24
        else
            imageUri.value
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = "ProfilePic",
                modifier = Modifier.wrapContentSize(),
                contentScale = ContentScale.Crop


            )
        }
        if(editmode)
            Button(onClick = {setChangePhotoExpanded(true)}){
            Text(text = "Change Profile Picture!")

            DropdownMenu(
                expanded = changephotoexpanded,
                onDismissRequest = { setChangePhotoExpanded (false) }
            ) {
                DropdownMenuItem(
                    text = {  Text("Select From gallery") },
                    onClick = { launcher.launch("image/*")
                                setChangePhotoExpanded(false)}
                )
                Divider()
                DropdownMenuItem(
                    text = { Text("Take from Camera") },
                    onClick = { checkpermission()
                        photo.launch()
                    }
                )

            }

        }

    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myTopBar(editmode : Boolean, setEditMode : (Boolean)->Unit, u: User?, viewModel: ProfileViewModel
             , name:String, nickname: String, mail:String, birthdate:String, sex:String, city:String){
    TopAppBar(
        title = { Text(text = "Your Profile") },

        actions = {

            IconButton(onClick = {
                if(editmode){
                    if(u === null) {
                        val insertuser= User(nickname = nickname, fullname = name, mail = mail, birthdate = birthdate, sex = sex, city = city)
                            viewModel.insertUser(insertuser)

                    }
                    else{

                        val updateduser = User(nickname=nickname, fullname = name, city = city, mail = mail, sex =sex, birthdate = birthdate);
                        viewModel.saveUpdatedUser(updateduser);
                    }

                }

                setEditMode(!editmode)

            }, enabled = true) {
                Icon(imageVector = if(editmode) Icons.Default.Check else Icons.Default.Edit, contentDescription = null)
            }

        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Green, titleContentColor = Color.Black)
    )
}


