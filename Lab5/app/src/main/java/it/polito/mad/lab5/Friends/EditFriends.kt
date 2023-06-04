package it.polito.mad.lab5.Friends

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditFriends(friendsViewModel: FriendsViewModel){
    var text =  friendsViewModel.text.value
    val searchingfriend = friendsViewModel.searching_firends

    Scaffold(
        topBar = { FriendsTopBar(viewModel = friendsViewModel)
        },
        content = {


            Column(modifier = Modifier.padding(it)) {

              TextField(value = text, onValueChange = {alfa ->
                  friendsViewModel.text.value=alfa
                  friendsViewModel.searchFriend(alfa)
              })

                Row() {
                    Text(text = "Your Friends", modifier = Modifier.padding(bottom = 8.dp))
                    Spacer(modifier = Modifier.width( 8.dp))
                }



                searchingfriend.forEach { friend ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {

                        Text(text = friend.name)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = friend.nickname)
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { friendsViewModel.addPending(friend) }) {
                            Text(text = "Add Friend");
                        }
                    }
                }

        }})

}