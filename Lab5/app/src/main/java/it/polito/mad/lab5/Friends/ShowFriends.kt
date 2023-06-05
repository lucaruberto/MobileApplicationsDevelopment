package it.polito.mad.lab5.Friends

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import it.polito.mad.lab5.db.Friend
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import it.polito.mad.lab5.db.ProvaUser
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.mad.lab5.R

@Composable
fun ShowFriends(friendsViewModel: FriendsViewModel){
    val db = FirebaseFirestore.getInstance()
    val acceptedFriends = friendsViewModel.friends_id
    val pendingRequests = friendsViewModel.pending_id

    Scaffold(
        topBar = { FriendsTopBar(viewModel = friendsViewModel)
        },
        content = {it ->

            Column(modifier = Modifier
                .padding(it)
                .fillMaxWidth()) {
                    if(pendingRequests.isNotEmpty()) {
                        Text(
                            text = "Pending Requests",
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 8.dp),
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        pendingRequests.forEach { friend ->
                            var user by remember(friend.id) {
                                mutableStateOf(ProvaUser())
                            }
                            LaunchedEffect(friend.id) {
                                val retrievedUser = friendsViewModel.getUserbyId(friend.id)
                                user = retrievedUser
                            }
                            val painter = rememberAsyncImagePainter(
                                if (user.imageUri.isEmpty())
                                    R.drawable.baseline_person_24
                                else
                                    Uri.parse(user.imageUri)
                            )
                            Card(elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                                shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Image(painter = painter,contentDescription = "ProfilePicA", modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape),
                                        contentScale = ContentScale.Crop )
                                    Column {
                                        Text(text = user.name, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.padding(top = 8.dp))
                                        Text(text = user.nickname, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(1f)) {
                                        FloatingActionButton(
                                            onClick = { friendsViewModel.addFriend(friend.id) },
                                            shape = RoundedCornerShape(50),
                                            elevation = elevation(16.dp),
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.Green
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = "Add"
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        FloatingActionButton(
                                            onClick = { friendsViewModel.deletePending(friend.id) },
                                            shape = RoundedCornerShape(50),
                                            elevation = elevation(16.dp),
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.Red
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = "Remove"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Text(
                        text = "Accepted Friends",
                        modifier = Modifier.padding(top = 16.dp, start = 8.dp),
                        fontSize = 32.sp
                    )
                Spacer(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))


                acceptedFriends.forEach { friend ->
                    var user by remember(friend.id) {
                        mutableStateOf(ProvaUser())
                    }
                    LaunchedEffect(friend.id) {
                        val retrievedUser = friendsViewModel.getUserbyId(friend.id)
                        user = retrievedUser
                    }
                    val painter = rememberAsyncImagePainter(
                        if (user.imageUri.isEmpty())
                            R.drawable.baseline_person_24
                        else
                            Uri.parse(user.imageUri)
                    )
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painter,contentDescription = "ProfilePicA", modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                                contentScale = ContentScale.Crop )
                            Column() {
                                Text(text = user.name, fontSize = 20.sp)
                                Spacer(modifier = Modifier.padding(top = 8.dp))
                                Text(text = user.nickname, fontSize = 12.sp)
                            }
                            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(1f)) {
                                Button(onClick = { friendsViewModel.deleteFriend(friend.id) }) {
                                    Text(text = "Remove")
                                }
                            }
                        }
                    }
                }
            }

        })

}