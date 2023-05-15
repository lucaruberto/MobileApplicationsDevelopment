package it.polito.mad.lab4.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import it.polito.mad.lab4.db.Sports
import it.polito.mad.lab4.db.SportsDao


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSportsDialog(
    availableSports: List<Sports>,
    selectedSports: MutableList<Sports>,
    onAddSport: (Sports) -> Unit,
    onDismissRequest: () -> Unit,
    setShowDialog: (Boolean) -> Unit,
    selectedSportLevel: MutableList<SportList>,
) {
    val filteredSports = remember { mutableStateOf(availableSports.filter {x-> selectedSportLevel.toList().none{x.discipline===it.sportname} }) }
    val searchText = remember { mutableStateOf("") }
    val selectedLevel = remember { mutableStateOf(1) }
    val oldSportslevel = remember {
       mutableStateListOf<SportList>()
    }.apply { addAll(selectedSportLevel) }

    val oldSports = remember {
        mutableStateListOf<Sports>()
    }.apply { addAll(selectedSports) }
    var context = LocalContext.current
    println("Sport disponibili:" + availableSports.toString())
    println("Sport gia scelti:" + selectedSportLevel.toList().toString())


    filteredSports.value = availableSports.filter {

        (it.discipline.contains(searchText.value, ignoreCase = true) && it !in selectedSports )
    }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest,
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                shape = RectangleShape
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Your sports",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        )
                        Row {
                            LazyRow() {
                                items(selectedSports) { sport ->
                                    val sportLevel =
                                        selectedSportLevel.find { it.sportname == sport.discipline }?.level
                                            ?: "None"
                                    SportCard(
                                        sport = sport,
                                        level = sportLevel.toString(),
                                        onLevelChanged = { level ->
                                            val updatedSportLevel =
                                                selectedSportLevel.map { sportList ->
                                                    if (sportList.sportname == sport.discipline) {
                                                        sportList.copy(level = level)
                                                    } else {
                                                        sportList
                                                    }
                                                }
                                        },
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                onAddSport(sport)
                                            }
                                            .fillMaxWidth(),
                                        selectedSport = selectedSports,
                                        add = false,
                                        selectedSportLevel = selectedSportLevel,
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Add a new sport",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        )
                        OutlinedTextField(
                            value = searchText.value,
                            onValueChange = { searchText.value = it },
                            label = { Text(text = "Search") },
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                                .width(150.dp),
                            singleLine = true
                        )
                        Row {
                            LazyRow() {
                                items(filteredSports.value) { sport ->
                                    val sportLevel =
                                        selectedSportLevel.find { it.sportname == sport.discipline }?.level
                                            ?: "None"
                                    SportCard(
                                        sport = sport,
                                        level = sportLevel.toString(),
                                        onLevelChanged = { level ->
                                            val updatedSportLevel =
                                                selectedSportLevel.map { sportList ->
                                                    if (sportList.sportname == sport.discipline) {
                                                        sportList.copy(level = level)
                                                    } else {
                                                        sportList
                                                    }
                                                }
                                        },
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                onAddSport(sport)
                                            }
                                            .fillMaxWidth(),
                                        selectedSport = selectedSports,
                                        add = true,
                                        selectedSportLevel = selectedSportLevel,
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = {
                            Toast.makeText(context,"All preferences Removed!", Toast.LENGTH_LONG).show()
                            selectedSportLevel.clear()
                            selectedSports.clear()
                            setShowDialog(false)
                        },colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary)) {
                            Text(text = "Discard")
                        }
                        Button(onClick = {
                            Toast.makeText(context,"Save and go back!", Toast.LENGTH_LONG).show()
                            setShowDialog(false)
                        }) {
                            Text(text = "Save")
                        }
                    }

                }

        }}
    )
}