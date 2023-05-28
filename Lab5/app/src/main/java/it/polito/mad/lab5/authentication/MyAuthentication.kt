package it.polito.mad.lab5.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.polito.mad.lab5.authentication.MyAuthenticationViewModel.ErrorType.*

@Composable
fun MyAuthentication(vm: MyAuthenticationViewModel) {
    val isRegistered = vm.isRegistered.value
    if(isRegistered){
        LoginScreen(vm = vm)
    }
    else{
        RegisterScreen(vm = vm)
    }
}

@Composable
fun RegisterScreen(vm: MyAuthenticationViewModel) {
    val error = vm.error.value
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        //email field
        TextField(value = vm.email.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = if (error.isError && error.type == EMAIL) 0.dp else 8.dp),
            label = { Text("Email") },
            onValueChange = {
            if (error.isError && error.type == EMAIL)
                vm.error.value.isError = false
            vm.email.value = it
        })

        //email field error
        if(error.isError && error.type == EMAIL) {
            // credential or password error during login
            Text(text = error.description, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        // password field
        TextField(value = vm.password.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = if (error.isError && error.type == PASSWORD) 0.dp else 8.dp),
            label = { Text("Password") }, onValueChange = {
            vm.password.value = it
            if (error.isError && error.type == PASSWORD)
                vm.error.value.isError = false
            else if (error.isError && error.type == REPEAT_PASSWORD)
                if(vm.password.value == vm.repeatPassword.value)
                    vm.error.value.isError = false
        })

        // password error field
        if(error.isError && error.type == PASSWORD) {
            // credential or password error during login
            Text(text = error.description, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        // repeat password field
        TextField(value = vm.repeatPassword.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = if (error.isError && error.type == REPEAT_PASSWORD) 0.dp else 8.dp),
            label = { Text("Repeat Password") }, onValueChange = {
            vm.repeatPassword.value = it
            if (error.isError && error.type == REPEAT_PASSWORD)
                if(vm.password.value == vm.repeatPassword.value)
                    vm.error.value.isError = false
        })

        // repeat password error field
        if(error.isError && error.type == REPEAT_PASSWORD) {
            // credential or password error during login
            Text(text = error.description, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        Button(onClick = { vm.firebaseSignUpWithEmailAndPassword() }) {
            Text(text = "Register")
        }
    }

}

@Composable
fun LoginScreen(vm: MyAuthenticationViewModel) {
    val error = vm.error.value
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)  {
        TextField(value = vm.email.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = if (error.isError && (error.type == EMAIL)) 0.dp else 8.dp),
            label = {Text(text = "Email")},
            onValueChange = {
            if (error.isError && (error.type == CREDENTIAL || error.type == EMAIL))
                vm.error.value.isError = false
            vm.email.value = it
        })

        if(error.isError && error.type == EMAIL) {
            //  error during login
            Text(text = error.description, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        TextField(value = vm.password.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = if (error.isError && (error.type == CREDENTIAL || error.type == PASSWORD)) 0.dp else 8.dp),
            label = {Text(text = "Password")},
            onValueChange = {
            if (error.isError && (error.type == CREDENTIAL || error.type == PASSWORD))
                vm.error.value.isError = false
            vm.password.value = it
        })

        if(error.isError && (error.type == CREDENTIAL || error.type == PASSWORD)) {
            // credential or password error during login
            Text(text = error.description, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        Button(onClick = { vm.firebaseSignInWithEmailAndPassword() }) {
            Text(text = "Sign In")
        }
        TextButton(onClick = { vm.isRegistered.value = false }) {
            Text(text = "Register")
        }
    }
}