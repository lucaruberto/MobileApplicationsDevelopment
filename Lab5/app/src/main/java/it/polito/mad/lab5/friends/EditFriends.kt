package it.polito.mad.lab5.friends

import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import it.polito.mad.lab5.R

@Composable
fun EditFriends(friendsViewModel: FriendsViewModel){
    var text by remember { mutableStateOf("") }
    val searchingfriend = friendsViewModel.searchingFriends

    Scaffold(
        topBar = { FriendsTopBar(viewModel = friendsViewModel)
        },
        content = { it ->
            Column(
                modifier = Modifier.fillMaxWidth().padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(32.dp),
                        singleLine = true,
                        label = { Text("Search a friend...") },
                        value = text,
                        onValueChange = { value ->
                            text = value
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                friendsViewModel.searchFriend(text)
                            }
                        )
                    )
                }

                Spacer(modifier = Modifier.width( 8.dp))

                searchingfriend.forEach { friend ->
                    val painter = if(friend.imageUri != "Loading"){
                        rememberAsyncImagePainter(
                            if (friend.imageUri.isEmpty())
                                R.drawable.baseline_person_24
                            else
                                Uri.parse(friend.imageUri)
                        )
                    }else {
                        val imageLoader = ImageLoader.Builder(LocalContext.current)
                            .components {
                                if (Build.VERSION.SDK_INT >= 28) {
                                    add(ImageDecoderDecoder.Factory())
                                } else {
                                    add(GifDecoder.Factory())
                                }
                            }
                            .build()
                        rememberAsyncImagePainter(R.drawable.loading, imageLoader)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = "ProfilePic",
                                modifier = Modifier
                                    .weight(1f)
                                    .size(64.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(4f)
                            ) {
                                Text(text = friend.name, fontSize = 20.sp)
                                Spacer(modifier = Modifier.padding(top = 8.dp))
                                Text(text = friend.nickname, fontSize = 12.sp)
                            }

                            Button(
                                modifier = Modifier.weight(1.5f),
                                onClick = { friendsViewModel.addPending(friend) }
                            ) {
                                Text(text = "Add")
                            }
                            Spacer(modifier = Modifier.width(8.dp))

                        }
                    }
                }
            }
        }
    )
}