package it.polito.mad.lab4.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import coil.compose.rememberAsyncImagePainter
import it.polito.mad.lab4.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path.toString())
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(checkpermission : ()->Unit, context: Context) {

    val name = "Mario Rossi"
    val email = "mario.rossi@email.com"

    val (editmode,setEditMode) = remember { mutableStateOf(false) }
    val (changephotoexpanded,setChangePhotoExpanded) = remember {
        mutableStateOf(false)
    }




    Scaffold(topBar = { myTopBar(editmode = editmode, setEditMode =setEditMode ) }, content = {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {

            ProfileImage(editmode,changephotoexpanded,setChangePhotoExpanded,checkpermission,context)

        }

    })






}


@Composable
fun ProfileImage(editmode : Boolean,changephotoexpanded :  Boolean,setChangePhotoExpanded : (Boolean)->Unit,checkpermission : ()->Unit,context: Context){
    val imageUri = rememberSaveable { mutableStateOf("") }

    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    var photoUri: Uri
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
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize(),

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
fun myTopBar(editmode : Boolean,setEditMode : (Boolean)->Unit){
    TopAppBar(
        title = { Text(text = "Your Profile") },

        actions = {

            IconButton(onClick = {
                println("provaa")
                setEditMode(!editmode)
            }, enabled = true) {
                Icon(imageVector = if(editmode) Icons.Default.Edit else Icons.Default.Check, contentDescription = null)
            }

        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Green, titleContentColor = Color.Black)
    )
}


