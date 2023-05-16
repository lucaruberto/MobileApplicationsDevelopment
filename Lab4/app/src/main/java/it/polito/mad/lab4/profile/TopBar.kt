package it.polito.mad.lab4.profile

import android.content.Context
import android.provider.CalendarContract.Colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myTopBar(
    editmode: Boolean, setEditMode: (Boolean) -> Unit, viewModel: ProfileViewModel
    , name: String, nickname: String, mail: String, birthdate: String, sex: String, city: String,
    saveUserData: (UserData, Context) -> Unit, user: UserData,
    context: Context,
    imageUri: String,
    selectedSportLevel: String)
{
    TopAppBar(
        title = { Text(text = "Your Profile" , textAlign = TextAlign.Center, color = Color.White) },

        actions = {

            IconButton(onClick = {
                if(editmode){
                    saveUserData(
                        UserData(fullName = name, nickname = nickname,mail = mail, birthdate = birthdate
                    , sex = sex, city = city, selectedSportLevel,imageUri
                        ), context = context)

                }

                setEditMode(!editmode)

            }
                , enabled = true) {
                Icon(imageVector = if(editmode) Icons.Default.Check else Icons.Default.Edit, contentDescription = null, tint = Color.White)
            }

        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = Color.Black)
    )
}

