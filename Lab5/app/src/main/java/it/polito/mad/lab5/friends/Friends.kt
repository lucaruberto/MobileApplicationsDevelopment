package it.polito.mad.lab5.friends

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable


@SuppressLint("UnrememberedMutableState")
@Composable
fun Friends(friendsViewModel: FriendsViewModel) {

    val editfriends = friendsViewModel.editFriends.value
    val acceptedFriends = friendsViewModel.friends_id
    val pendingRequests = friendsViewModel.pending_id

    if(editfriends){
        //Text(text = "Sei nella editfriends!")
        EditFriends(friendsViewModel = friendsViewModel)
    }
    else {
        ShowFriends(friendsViewModel = friendsViewModel)
    }
}


