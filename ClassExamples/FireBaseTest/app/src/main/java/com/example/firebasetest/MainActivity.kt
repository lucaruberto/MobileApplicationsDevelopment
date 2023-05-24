package com.example.firebasetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.layout.rememberLazyNearestItemsRangeState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firebasetest.ui.MyAuthentication
import com.example.firebasetest.ui.MyAuthenticationViewModel
import com.example.firebasetest.ui.theme.FireBaseTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = FireBaseViewModel()
        val authVm = MyAuthenticationViewModel()

        setContent {
            FireBaseTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(authVm.isLogged.value) MyFireBase(vm)
                    else MyAuthentication(vm = authVm)
                }
            }
        }
    }
}

@Composable
fun MyFireBase(vm: FireBaseViewModel) {
    val users = vm.users

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), onClick = {
                vm.addUsers()
                //vm.readUsers()
            }) {
                Text(text = "Add User")
            }
        }
        items(users){
            Text(modifier = Modifier.padding(16.dp), text = it)
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FireBaseTestTheme {
        Greeting("Android")
    }
}