package it.polito.mad.lab5.Friends

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditFriends(friendsViewModel: FriendsViewModel){
    var text =  friendsViewModel.text.value
    val searchingfriend = friendsViewModel.searching_firends

    Scaffold(
        topBar = { FriendsTopBar(viewModel = friendsViewModel)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                Row {
                    OutlinedTextField(
                        shape = RoundedCornerShape(32.dp),
                        singleLine = true,
                        label = { Text("Search") },
                        value = text, onValueChange = { value ->
                            text = value
                        })
                    Button(onClick = { friendsViewModel.searchFriend(text) }) {
                        Text(text = "Search")
                    }
                }

                    Text(text = "Your Friends", modifier = Modifier.padding(bottom = 8.dp), fontSize = 32.sp)
                    Spacer(modifier = Modifier.width( 8.dp))

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