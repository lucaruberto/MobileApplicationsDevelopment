package it.polito.mad.lab4.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import it.polito.mad.lab4.db.User

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

