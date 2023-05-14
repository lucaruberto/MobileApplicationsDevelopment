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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import it.polito.mad.lab4.db.User
import it.polito.mad.lab4.R
import it.polito.mad.lab4.db.Sports
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
    var (selectedSports,setSelectedSport) = remember { mutableStateOf(emptyList<Sports>()) }
    var showDialog by remember { mutableStateOf(false) }
    //valori di prova
    val sports= listOf<Sports>(Sports(1,"Calcio"), Sports(2,"Basket"), Sports(3,"Danza"));


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
            Button(onClick = {showDialog=true }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Modifica Sport")
            }
            /*if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    modifier = Modifier.fillMaxHeight(),
                    title = { Text(text = "Modifica sport") },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Barra di ricerca
                            TextField(
                                value = "",
                                onValueChange = { /* Aggiorna il valore della barra di ricerca */ },
                                label = { Text(text = "Cerca sport") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Elenco di sport selezionabili
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                items(sports) { sport ->
                                    SportCard(
                                        sport = sport,
                                        isSelected = selectedSports.contains(sport),
                                        onSelected = { isSelected ->
                                            if (isSelected) {
                                                selectedSports += sport
                                            } else {
                                                selectedSports -= sport
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Salva")
                        }
                    },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                )
            }
            */
            if(showDialog){
                SelectSportsDialog(
                    availableSports = sports,
                    selectedSports = selectedSports,
                    setSelectedSport= setSelectedSport,
                    onAddSport = { sport ->
                        // Do something when a sport is selected
                    },
                    onDismissRequest = {showDialog=false }
                )
            }

        }

    })






}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileField(hover:String,text:String,setText : (String)->Unit,editmode: Boolean)
{
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(50.dp)
            .width(250.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = hover,
                modifier = Modifier
                    .padding(top = 15.dp, end = 8.dp)
                    .width(100.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
           if(editmode)
            TextField(
                value = text,
                onValueChange = { setText(it) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxSize()
                    .height(48.dp),
                maxLines = 1,
                singleLine = true,
            )
            else
           {
                if(text==="")
                    Text(
                        text = "Not Inserted Yet",
                        modifier = Modifier.padding(top=15.dp,end = 8.dp),

                    )
               else
                    Text(
                        text = text,
                        modifier = Modifier.padding(top=15.dp,end = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                    )

           }
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
                modifier = Modifier.fillMaxWidth(1f),
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
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = Color.Black)
    )
}


@Composable
fun SportCard(
    sport: Sports,
    level: Int,
    onLevelChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    setSelectedSport: (List<Sports>) -> Unit,
    selectedSport: List<Sports>,
    add:Boolean
) {

    var (expandable,setExpandable) = remember { mutableStateOf(false) }
    var (testo, setTesto) = remember {
        mutableStateOf("None")
    }
    Card(
        modifier = modifier
            .height(100.dp)
            .width(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),

    ) {
        Row() {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(0.76f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = sport.discipline,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Text(
                        text = "Ability Level:",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(.60f)
                    )
                    Box(modifier= Modifier.weight(.40f)){
                        Text(text = testo,
                                modifier= Modifier.clickable { setExpandable(true) },
                             style = MaterialTheme.typography.bodySmall);
                        DropdownMenu(
                            expanded = expandable,
                            onDismissRequest = {setExpandable(false) },
                            modifier = Modifier.width(30.dp)
                        ) {
                            (1..5).forEach { level ->
                                DropdownMenuItem(
                                    text = { Text(text = "$level")}, onClick = {
                                        setTesto("$level")
                                        setExpandable(false)
                                    }, enabled = true)
                            }
                        }
                    }



                }
            }
            Column(modifier = Modifier.weight(0.24f)) {
                Button(modifier = Modifier
                    .padding(top = 40.dp)
                    .size(40.dp), onClick = {
                    if(add)
                    setSelectedSport(selectedSport.plus(sport))
                    else
                    setSelectedSport(selectedSport.filter { it!=sport })}) {
                    Text(text = "+")
                }
            }
        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSportsDialog(
    availableSports: List<Sports>,
    selectedSports: List<Sports>,
    onAddSport: (Sports) -> Unit,
    onDismissRequest: () -> Unit,
    setSelectedSport: (List<Sports>) -> Unit
) {
    val filteredSports = remember { mutableStateOf(availableSports) }
    val searchText = remember { mutableStateOf("") }
    val selectedLevel = remember { mutableStateOf(1) }

    filteredSports.value = availableSports.filter {
        it.discipline.contains(searchText.value, ignoreCase = true) && it !in selectedSports
    }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest,
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                shape = androidx.compose.ui.graphics.RectangleShape
            ) {

                Row(modifier = Modifier.height(50.dp)) {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp)) {

                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "Selected Sports",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        )
                        LazyColumn(Modifier.weight(1f)) {
                            items(selectedSports) { sport ->
                                SportCard(
                                    sport = sport,
                                    level = 1,
                                    onLevelChanged = { level ->
                                        selectedLevel.value=level
                                    },
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            onAddSport(sport)
                                        }
                                        .fillMaxWidth(),
                                    setSelectedSport = setSelectedSport,
                                    selectedSport = selectedSports,
                                    add = false
                                )
                            }
                        }
                    }
                    Column(Modifier.weight(1f)) {
                        TextField(
                            value = searchText.value,
                            onValueChange = { searchText.value = it },
                            label = { Text(text = "Search for sport") },
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            maxLines = 1,
                            singleLine = true

                        )
                        LazyColumn(Modifier.weight(1f)) {
                            items(filteredSports.value) { sport ->
                                SportCard(
                                    sport = sport,
                                    level = selectedLevel.value,
                                    onLevelChanged = { level ->
                                        selectedLevel.value = level
                                    },
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            onAddSport(sport)
                                        }
                                        .fillMaxWidth(),
                                    setSelectedSport=setSelectedSport,
                                    selectedSport=selectedSports,
                                    add = true
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}