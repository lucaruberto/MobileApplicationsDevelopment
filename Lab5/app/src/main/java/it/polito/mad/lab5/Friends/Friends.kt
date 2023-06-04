package it.polito.mad.lab5.Friends

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@SuppressLint("UnrememberedMutableState")
@Composable
fun Friends(friendsViewModel: FriendsViewModel) {

    val editfriends = friendsViewModel.editFriends.value
    val acceptedFriends = friendsViewModel.friends_id
    val pendingRequests = friendsViewModel.pending_id

    if(editfriends){
        
        Text(text = "Sei nella editfriends!");
        EditFriends(friendsViewModel = friendsViewModel)

    }
    else
    {
    ShowFriends(friendsViewModel = friendsViewModel)
    }
}


