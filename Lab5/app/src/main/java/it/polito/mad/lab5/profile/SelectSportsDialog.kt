package it.polito.mad.lab5.profile

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import it.polito.mad.lab5.db.ProvaSport
import it.polito.mad.lab5.db.UserSports


@Composable
fun SelectSportsDialog(
    allSports: MutableList<ProvaSport>,
    selectedSports: SnapshotStateList<UserSports>,
    onDismissRequest: () -> Unit,
    setShowDialog: (Boolean) -> Unit,
) {
    //val filteredSports = remember { mutableStateOf(allSports.filter {x-> selectedSports.toList().none{x.discipline===it.sportName} }) }
    /*Log.d(TAG, "Selected Sports: ")
    selectedSports.forEach(){
        Log.d(TAG, "${it.sportName}")
    }*/
    val filteredSports = allSports
        .filter { x-> selectedSports.toList().none{x.discipline==it.sportName} }
    Log.d(TAG, "Filtered Sports: ")
    filteredSports.forEach{
        Log.d(TAG, it.discipline)
    }
    val searchText = remember { mutableStateOf("") }
    val context = LocalContext.current


    /*
    filteredSports.value = availableSports.filter {
        (it.discipline.contains(searchText.value, ignoreCase = true) &&  selectedSports.none{x-> x.discipline==it.discipline} )
    }*/

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
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp).align(CenterHorizontally)
                        )
                        Row {
                            LazyColumn (modifier = Modifier.weight(1f)){
                                items(selectedSports) { sport ->
                                    /*val sportLevel =
                                        selectedSports.find { it.sportName == sport.discipline }?.level
                                            ?: "None"*/
                                    SportCard(
                                        sportName = sport.sportName,
                                        level = sport.level,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        selectedSports = selectedSports,
                                        add = false
                                        //selectedSportLevel = selectedSportLevel,
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Add a new sport",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp).align(CenterHorizontally)

                        )

                        Row {
                            LazyColumn (modifier = Modifier.weight(2f)){
                                items(filteredSports) { sport ->
                                    /*val sportLevel =
                                        selectedSportLevel.find { it.SportName == sport.discipline }?.Level
                                            ?: "None"*/
                                    SportCard(
                                        sportName = sport.discipline,
                                        level = "None",
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        selectedSports = selectedSports,
                                        add = true,
                                        //selectedSportLevel = selectedSportLevel,
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