package it.polito.mad.lab4.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import it.polito.mad.lab4.R

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