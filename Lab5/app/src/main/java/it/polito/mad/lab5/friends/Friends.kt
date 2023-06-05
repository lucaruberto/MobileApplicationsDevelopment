package it.polito.mad.lab5.friends

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import it.polito.mad.lab5.profile.ProfileViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun Friends(friendsViewModel: FriendsViewModel, profileViewModel: ProfileViewModel) {

    val editfriends = friendsViewModel.editFriends.value
    val acceptedFriends = friendsViewModel.friendsId
    val pendingRequests = friendsViewModel.pendingId

    if(editfriends){
        //Text(text = "Sei nella editfriends!")
        EditFriends(friendsViewModel = friendsViewModel, profileViewModel)
    }
    else {
        ShowFriends(friendsViewModel = friendsViewModel)
    }
}


