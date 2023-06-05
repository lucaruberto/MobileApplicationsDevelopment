package it.polito.mad.lab5.friends

import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import it.polito.mad.lab5.db.ProvaUser
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.mad.lab5.R

@Composable
fun ShowFriends(friendsViewModel: FriendsViewModel){
    val db = FirebaseFirestore.getInstance()
    val acceptedFriends = friendsViewModel.friendsId
    val pendingRequests = friendsViewModel.pendingId

    Scaffold(
        topBar = { FriendsTopBar(viewModel = friendsViewModel)
        },
        content = {it ->

            Column(modifier = Modifier
                .padding(it)
                .fillMaxWidth()
            ) {
                    if(pendingRequests.isNotEmpty()) {
                        Text(
                            text = "Pending Requests",
                            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                            fontSize = 26.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        pendingRequests.forEach { friend ->
                            val (user,setUser) = remember(friend.id) {
                                mutableStateOf(ProvaUser())
                            }
                            LaunchedEffect(friend.id) {
                                friendsViewModel.getUserById(friend.id, setUser)
                            }

                            /*


    Image(
        painter = rememberAsyncImagePainter(R.drawable.cat, imageLoader),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
                             */

                            val painter = if(user.imageUri != "Loading"){
                                rememberAsyncImagePainter(
                                    if (user.imageUri.isEmpty())
                                        R.drawable.baseline_person_24
                                    else
                                        Uri.parse(user.imageUri)
                                )
                            }else {
                                val imageLoader = ImageLoader.Builder(LocalContext.current)
                                    .components {
                                        if (SDK_INT >= 28) {
                                            add(ImageDecoderDecoder.Factory())
                                        } else {
                                            add(GifDecoder.Factory())
                                        }
                                    }
                                    .build()
                                rememberAsyncImagePainter(R.drawable.loading, imageLoader)
                            }

                            Card(elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                                shape = RoundedCornerShape(32.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Image(painter = painter,contentDescription = "ProfilePicA", modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape),
                                        contentScale = ContentScale.Crop )
                                    Column (modifier = Modifier.padding(start = 16.dp)){
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
                                            containerColor = Color.LightGray,
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
                Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Friends",
                        modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                        fontSize = 26.sp
                    )
                acceptedFriends.forEach { friend ->
                    val (user, setUser) = remember(friend.id) { mutableStateOf(ProvaUser()) }
                    LaunchedEffect(friend.id) {
                        friendsViewModel.getUserById(friend.id, setUser)
                    }
                    val painter = if(user.imageUri != "Loading"){
                        rememberAsyncImagePainter(
                            if (user.imageUri.isEmpty())
                                R.drawable.baseline_person_24
                            else
                                Uri.parse(user.imageUri)
                        )
                    }else {
                        val imageLoader = ImageLoader.Builder(LocalContext.current)
                            .components {
                                if (SDK_INT >= 28) {
                                    add(ImageDecoderDecoder.Factory())
                                } else {
                                    add(GifDecoder.Factory())
                                }
                            }
                            .build()
                        rememberAsyncImagePainter(R.drawable.loading, imageLoader)
                    }
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painter,contentDescription = "ProfilePicA", modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                                contentScale = ContentScale.Crop )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
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