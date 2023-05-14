package it.polito.mad.lab4.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import it.polito.mad.lab4.db.Sports

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSportsDialog(
    availableSports: List<Sports>,
    selectedSports: List<Sports>,
    onAddSport: (Sports) -> Unit,
    onDismissRequest: () -> Unit,
    setSelectedSport: (List<Sports>)->Unit,
    setShowDialog: (Boolean) -> Unit,
    selectedSportLevel: List<SportList>,
    setSelectedSportLevel: (List<SportList>)->Unit
) {
    val filteredSports = remember { mutableStateOf(availableSports) }
    val searchText = remember { mutableStateOf("") }
    val selectedLevel = remember { mutableStateOf(1) }


    filteredSports.value = availableSports.filter {
        it.discipline.contains(searchText.value, ignoreCase = true) && it !in selectedSports
    }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest,
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                shape = RectangleShape
            ) {

                Row(modifier = Modifier.height(50.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = {setShowDialog(false)}) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(onClick = { /*TODO*/ },) {
                        Text(text = "Save Changes!");

                    }
                }

                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp)) {

                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "Selected Sports",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        )
                        LazyColumn(Modifier.weight(1f)) {
                            items(selectedSports) { sport ->
                                println("diocandiocan")
                                for (i in selectedSportLevel.listIterator())
                                {
                                    println("i= ")
                                    println(i)
                                    if(i.sportname===sport.discipline)
                                    {
                                        println("viva la figa")
                                    }
                                }
                                SportCard(
                                    sport = sport,
                                    level =selectedLevel.value,
                                    onLevelChanged = { level ->
                                       //
                                    },
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            onAddSport(sport)
                                        }
                                        .fillMaxWidth(),
                                    setSelectedSport = setSelectedSport,
                                    selectedSport = selectedSports,
                                    add = false,
                                    selectedSportLevel = selectedSportLevel,
                                    setSelectedSportLevel = setSelectedSportLevel
                                )
                            }
                        }
                    }
                    Column(Modifier.weight(1f)) {
                        TextField(
                            value = searchText.value,
                            onValueChange = { searchText.value = it },
                            label = { Text(text = "Search for sport") },
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            maxLines = 1,
                            singleLine = true

                        )
                        LazyColumn(Modifier.weight(1f)) {
                            items(filteredSports.value) { sport ->
                                SportCard(
                                    sport = sport,
                                    level = selectedLevel.value,
                                    onLevelChanged = { level ->
                                        //
                                    },
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            onAddSport(sport)
                                        }
                                        .fillMaxWidth(),
                                    setSelectedSport =setSelectedSport,
                                    selectedSport =selectedSports,
                                    add = true,
                                    selectedSportLevel = selectedSportLevel,
                                    setSelectedSportLevel = setSelectedSportLevel
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}