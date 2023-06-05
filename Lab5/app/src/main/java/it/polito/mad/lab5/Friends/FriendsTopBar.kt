package it.polito.mad.lab5.Friends

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import it.polito.mad.lab5.profile.ProfileViewModel


@Composable
fun FriendsTopBar(viewModel: FriendsViewModel) {

    val editfriends = viewModel.editFriends.value

    TopAppBar(
        title = {Text(text = if(!editfriends) "Your Friends" else "Search", textAlign = TextAlign.Center) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary),
        actions = {
            IconButton(
                onClick = {
                    viewModel.editFriends.value = !editfriends
                },
                enabled = true
            ) {
                Icon(
                    imageVector = if(!viewModel.editFriends.value) {
                        Icons.Default.Search
                    } else {
                        Icons.Filled.Close
                    },
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}