package it.polito.mad.lab5.profile

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
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(viewModel: ProfileViewModel)
{
    val editMode = viewModel.editMode.value
    TopAppBar(
        title = { Text(text = "Your Profile" , textAlign = TextAlign.Center, color = Color.White) },

        actions = {

            IconButton(
                onClick = {
                    if(editMode){
                        viewModel.updateUser()
                        viewModel.updateUserSports()
                    }
                    viewModel.editMode.value = !editMode
                },
                enabled = true
            ) {
                Icon(
                    imageVector =
                        if(editMode)
                            Icons.Default.Check
                        else
                            Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary)
    )
}

