package com.example.firebasetest

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firebasetest.ui.MyAuthentication
import com.example.firebasetest.ui.MyAuthenticationViewModel
import com.example.firebasetest.ui.theme.FireBaseTestTheme

class MainActivity : ComponentActivity() {

    val vm = FireBaseViewModel()
    val authVm = MyAuthenticationViewModel { vm.startListenerRegistration() }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFireBase(vm: FireBaseViewModel) {
    val text = vm.text.value
    val texts = vm.texts

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            TextField(value = text, onValueChange = { vm.text.value = it}, modifier = Modifier.padding(16.dp))
        }
        item {
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), onClick = {
                vm.addText()
                //vm.readUsers()
            }) {
                Text(text = "Send Text")
            }
        }
        items(texts){
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