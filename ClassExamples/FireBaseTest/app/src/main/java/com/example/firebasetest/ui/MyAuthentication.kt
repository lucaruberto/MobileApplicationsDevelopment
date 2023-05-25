package com.example.firebasetest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(vm: MyAuthenticationViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(value = vm.email.value, label = { Text("Email") }, onValueChange = {vm.email.value = it})
        TextField(value = vm.password.value, label = { Text("Password") }, onValueChange = {vm.password.value = it})
        Row(verticalAlignment = Alignment.CenterVertically){
            TextField(value = vm.repeatPassword.value, label = { Text("Repeat Password") }, onValueChange = {
                vm.repeatPassword.value = it
                if(it == vm.password.value)
                    vm.equalPasswords.value = 1
                else
                    vm.equalPasswords.value = -1
            })
            if(vm.equalPasswords.value == 1)
                Icon(modifier = Modifier.padding(16.dp), imageVector = Icons.Rounded.Check, contentDescription = "Passwords are equal", tint = Color.Green)
            else if (vm.equalPasswords.value == 1)
                Icon(modifier = Modifier.padding(16.dp), imageVector = Icons.Rounded.Close, contentDescription = "Passwords are different", tint = Color.Red)
        }
        Button(onClick = { if(vm.equalPasswords .value == 1) vm.firebaseSignUpWithEmailAndPassword() }) {
            Text(text = "Register")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: MyAuthenticationViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)  {
        TextField(value = vm.email.value, onValueChange = {vm.email.value = it})
        TextField(value = vm.password.value, onValueChange = {vm.password.value = it})
        Button(onClick = { vm.firebaseSignInWithEmailAndPassword() }) {
            Text(text = "Sign In")
        }
        TextButton(onClick = { vm.isRegistered.value = false }) {
            Text(text = "Register")
        }
    }
}