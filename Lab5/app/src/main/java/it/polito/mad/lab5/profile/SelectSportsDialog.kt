package it.polito.mad.lab5.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import it.polito.mad.lab5.db.ProvaSports
import it.polito.mad.lab5.db.ProvaUserSports
import it.polito.mad.lab5.db.Sports


@Composable
fun SelectSportsDialog(
    availableSports: MutableList<ProvaSports>,
    selectedSports: MutableList<Sports>,
    onDismissRequest: () -> Unit,
    setShowDialog: (Boolean) -> Unit,
    selectedSportLevel: SnapshotStateList<ProvaUserSports>,
) {
    val filteredSports = remember { mutableStateOf(availableSports.filter {x-> selectedSportLevel.toList().none{x.discipline===it.SportName} }) }
    val searchText = remember { mutableStateOf("") }
    val context = LocalContext.current



    filteredSports.value = availableSports.filter {
        (it.discipline.contains(searchText.value, ignoreCase = true) &&  selectedSports.none{x-> x.discipline==it.discipline} )
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
                            LazyRow {
                                items(selectedSports) { sport ->
                                    val sportLevel =
                                        selectedSportLevel.find { it.SportName == sport.discipline }?.Level
                                            ?: "None"
                                    SportCard(
                                        sport = sport,
                                        level = sportLevel,
                                        modifier = Modifier
                                            .padding(8.dp)
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
                            LazyRow {
                                items(filteredSports.value) { sport ->
                                    val sportLevel =
                                        selectedSportLevel.find { it.SportName == sport.discipline }?.Level
                                            ?: "None"
                                    SportCard(
                                        sport = Sports(discipline = sport.discipline),
                                        level = sportLevel,
                                        modifier = Modifier
                                            .padding(8.dp)
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