package it.polito.mad.lab5.Friends

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import it.polito.mad.lab5.R
import it.polito.mad.lab5.db.ProvaUser

@Composable
fun EditFriends(friendsViewModel: FriendsViewModel){
    var text by remember { mutableStateOf("") }
    val searchingfriend = friendsViewModel.searching_friends

    Scaffold(
        topBar = { FriendsTopBar(viewModel = friendsViewModel)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                Row(modifier = Modifier.padding(8.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(32.dp),
                        singleLine = true,
                        label = { Text("Search") },
                        value = text, onValueChange = { value ->
                            text = value
                        })
                    Button(onClick = { friendsViewModel.searchFriend(text) }, modifier = Modifier.padding(start = 8.dp, top = 8.dp).align(
                        Alignment.CenterVertically)) {
                        Text(text = "Search")
                    }
                }

                    Spacer(modifier = Modifier.width( 8.dp))

                searchingfriend.forEach { friend ->

                    val painter = rememberAsyncImagePainter(
                        if (friend.imageUri.isEmpty())
                            R.drawable.baseline_person_24
                        else
                            Uri.parse(friend.imageUri)
                    )
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                    ) {
                        Image(painter = painter,contentDescription = "ProfilePicA", modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                            contentScale = ContentScale.Crop )
                        Column() {
                            Text(text = friend.name, fontSize = 20.sp)
                            Spacer(modifier = Modifier.padding(top = 8.dp))
                            Text(text = friend.nickname, fontSize = 12.sp)
                        }
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(1f)) {
                            Button(onClick = { friendsViewModel.addPending(friend) }) {
                                Text(text = "Add");
                            }
                        }
                    }
                    }
                }

        }})

}