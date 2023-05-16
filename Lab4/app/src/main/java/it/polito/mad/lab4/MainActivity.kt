package it.polito.mad.lab4

import Rent
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.mad.lab4.profile.Profile
import it.polito.mad.lab4.profile.UserData
import it.polito.mad.lab4.rate.Rate
import it.polito.mad.lab4.reservation.Reservation
import it.polito.mad.lab4.ui.theme.Lab4Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        var nickname = sharedPref.getString("nickname", "")
        var fullname = sharedPref.getString("fullname", "")
        var email = sharedPref.getString("mail", "")
        var birth = sharedPref.getString("birthdate", "")
        var  sex = sharedPref.getString("sex", "")
        var city = sharedPref.getString("city", "")
        var imageUri = sharedPref.getString("imageUri","")
        var sportlist = sharedPref.getString("sportlist","")
        fullname=  if(fullname=== null) "" else fullname
        nickname=  if(nickname=== null) "" else nickname
        email=  if(email=== null) "" else email
        birth=  if(birth=== null) "" else birth
        sex=  if(sex=== null) "" else sex
        city=  if(city=== null) "" else city
        imageUri = if(imageUri=== null)"" else imageUri
        sportlist = if(sportlist===null)"" else sportlist

        val user = UserData(fullName = fullname,nickname=nickname,mail=email, birthdate = birth, city = city, sex = sex, selectedSportsLevel =sportlist , imageUri = imageUri)



       val checkpermission = { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                   Manifest.permission.WRITE_EXTERNAL_STORAGE
               )
               == PackageManager.PERMISSION_DENIED
           ) {
               val permission =
                   arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
               requestPermissions(permission, 112)
           }
       }}

        super.onCreate(savedInstanceState)
        setContent {
            Lab4Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Mainscreen(checkpermission,user)
                }
            }
        }
    }
}
@Composable
fun Mainscreen(checkpermission: () -> Unit, user: UserData){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomAppBar( ){

               Row(Modifier.fillMaxWidth()){
                   Column(
                       modifier = Modifier.weight(1f),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {
                       Button(onClick = { navController.navigate("ScreenOne") }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)) {
                           Icon(
                               Icons.Sharp.Person,
                               contentDescription = "Profile",
                               modifier = Modifier.size(ButtonDefaults.IconSize),
                               tint = Color.Black
                           )
                       }
                       Text("Profile", color = MaterialTheme.colorScheme.primary)

                   }

                   Column( modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                       Button(onClick = { navController.navigate("ScreenThree") }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)) {
                           Icon(
                               Icons.Sharp.Search,
                               contentDescription = "Rent",
                               modifier = Modifier.size(ButtonDefaults.IconSize),
                               tint = Color.Black
                           )
                       }
                       Text("Add", color = MaterialTheme.colorScheme.primary)

                   }

                    Column( modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { navController.navigate("ScreenTwo") }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)  ) {
                            Icon(
                                Icons.Sharp.DateRange,
                                contentDescription = "Calendar",
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                tint = Color.Black
                            )
                        }
                        Text("Reserved", color = MaterialTheme.colorScheme.primary)

                    }
                   Column( modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                       Button(onClick = { navController.navigate("ScreenFour") }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)) {
                           Icon(
                               Icons.Sharp.Star,
                               contentDescription = "Rate",
                               modifier = Modifier.size(ButtonDefaults.IconSize),
                               tint = Color.Black
                           )
                       }
                       Text("Reviews", color = MaterialTheme.colorScheme.primary)

                   }
                }
            }
        }
    ) {
        Box(Modifier.padding(it)){
            NavHost(navController = navController, startDestination = "ScreenOne"){
                composable("ScreenOne"){ Profile(checkpermission, LocalContext.current,user)}
                composable("ScreenTwo"){ Reservation()}
                composable("ScreenThree"){ Rent()}
                composable("ScreenFour") {Rate()}
            }
        }
    }
}

/*
@Composable
fun Profile(){
    Box(Modifier.fillMaxSize()){
        Text("Screen One",
            modifier = Modifier.align(Alignment.Center),
            style= MaterialTheme.typography.headlineLarge)
    }
}
*/
