package it.polito.mad.lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material.icons.sharp.Place
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.mad.lab4.ui.theme.Lab4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab4Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Mainscreen()
                }
            }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mainscreen(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomAppBar(Modifier.background(MaterialTheme.colorScheme.primary) ){

               Row(Modifier.fillMaxWidth()){
                   Column( modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, ) {
                       Button(onClick = { navController.navigate("ScreenOne") }) {
                           Icon(
                               Icons.Sharp.Person,
                               contentDescription = "Profile",
                               modifier = Modifier.size(ButtonDefaults.IconSize)
                           )
                       }
                       Text("Profile", color = MaterialTheme.colorScheme.primary)

                   }

                    Column( modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { navController.navigate("ScreenTwo") }, ) {
                            Icon(
                                Icons.Sharp.Place,
                                contentDescription = "Calendar",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                        }
                        Text("Reservations", color = MaterialTheme.colorScheme.primary)

                    }

                   Column( modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { navController.navigate("ScreenThree") }) {
                            Icon(
                                Icons.Sharp.Search,
                                contentDescription = "Rent",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                        }
                        Text("Rent", color = MaterialTheme.colorScheme.primary)

                    }
                   Column( modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                       Button(onClick = { navController.navigate("ScreenFour") }) {
                           Icon(
                               Icons.Sharp.Star,
                               contentDescription = "Rate",
                               modifier = Modifier.size(ButtonDefaults.IconSize)
                           )
                       }
                       Text("Rent", color = MaterialTheme.colorScheme.primary)

                   }
                }
            }
        }
    ) {
        Box(Modifier.padding(it)){
            NavHost(navController = navController, startDestination = "ScreenOne"){
                composable("ScreenOne"){ Profile()}
                composable("ScreenTwo"){ Reservations()}
                composable("ScreenThree"){ Rent()}
                composable("ScreenFour") {Rate()}
            }
        }
    }
}

@Composable
fun Profile(){
    Box(Modifier.fillMaxSize()){
        Text("Screen One",
            modifier = Modifier.align(Alignment.Center),
            style= MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun Reservations(){
    Box(Modifier.fillMaxSize()){
        Text("Screen Two",
            modifier = Modifier.align(Alignment.Center),
            style= MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun Rent(){
    Box(Modifier.fillMaxSize()){
        Text("Screen Three",
            modifier = Modifier.align(Alignment.Center),
            style= MaterialTheme.typography.headlineLarge)
    }
}
@Composable
fun Rate(){
    Box(Modifier.fillMaxSize()){
        Text("Screen Four",
            modifier = Modifier.align(Alignment.Center),
            style= MaterialTheme.typography.headlineLarge)
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab4Theme {
        Greeting("Android")
    }
}