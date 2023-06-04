package it.polito.mad.lab5.Friends

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import it.polito.mad.lab5.db.Friend
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.polito.mad.lab5.db.ProvaUser

@Composable
fun ShowFriends(friendsViewModel: FriendsViewModel){


    val acceptedFriends = friendsViewModel.friends_id

    val pendingRequests = friendsViewModel.pending_id

    Scaffold(
        topBar = { FriendsTopBar(viewModel = friendsViewModel)
        },
        content = {it ->


            Column(modifier = Modifier
                .padding(it)
                .fillMaxWidth()) {
                Row() {
                    Text(text = "Pending Requests", modifier = Modifier.padding(bottom = 8.dp))
                    Spacer(modifier = Modifier.width( 8.dp))
                }



                    pendingRequests.forEach { friend ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {

                            Text(text = "Id del friendo")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = friend.state)
                            Spacer(modifier = Modifier.width(4.dp))
                            Button(onClick = { friendsViewModel.addFriend(friend.id) }) {
                                Text(text = "V")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { friendsViewModel.deletePending(friend.id) }) {
                                Text(text = "X")
                            }
                        }
                    }
                    Text(
                        text = "Accepted Friends",
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )


                acceptedFriends.forEach { friend ->

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {


                        Column {
                            Text(text = friend.id)

                            Button(onClick = { friendsViewModel.deleteFriend(friend.id) }) {
                                Text(text = "Remove Friend")

                            }
                        }
                    }
                }
            }

        })

}