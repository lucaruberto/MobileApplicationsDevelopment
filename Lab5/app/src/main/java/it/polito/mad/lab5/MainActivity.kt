package it.polito.mad.lab5

import Rent
import android.Manifest
import android.app.Application
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import it.polito.mad.lab5.authentication.MyAuthentication
import it.polito.mad.lab5.authentication.MyAuthenticationViewModel
import it.polito.mad.lab5.profile.Profile
import it.polito.mad.lab5.profile.ProfileViewModel
import it.polito.mad.lab5.rate.Rate
import it.polito.mad.lab5.reservation.Reservation
import it.polito.mad.lab5.reservation.ShowReservationsViewModel
import it.polito.mad.lab5.ui.theme.Lab5Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val authVm = MyAuthenticationViewModel()

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val permission =
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, 112)
        }


        super.onCreate(savedInstanceState)
        setContent {
            Lab5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(authVm.isLogged.value)
                        MainScreen(application)
                    else
                        MyAuthentication(vm = authVm)
                }
            }
        }
    }
}


@Composable
fun MainScreen(application: Application) {
    Log.d(TAG, "Loading main screen")
    val navController = rememberNavController()
    val profileViewModel = ProfileViewModel(application = application)
    val reservationsViewModel = ShowReservationsViewModel(application = application)
    profileViewModel.fetchInitialData()
    Scaffold(
        bottomBar = {
            BottomAppBar{

               Row(Modifier.fillMaxWidth()){
                   Column(
                       modifier = Modifier.weight(1f),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {
                       Button(onClick = {
                           navController.navigate("ScreenOne") }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)) {
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
                composable("ScreenOne"){
                    Log.d(TAG, "Entering Profile Screen")
                    Profile(LocalContext.current, profileViewModel)
                }
                composable("ScreenTwo"){
                    Log.d(TAG, "Entering Reservations Screen")
                    Reservation(reservationsViewModel)
                }
                composable("ScreenThree"){ Rent()}
                composable("ScreenFour") {Rate()}
            }
        }
    }
}

