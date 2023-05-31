import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kizitonwose.calendar.core.CalendarDay
import it.polito.mad.lab5.MyCalendar
import it.polito.mad.lab5.db.FasciaOraria
import it.polito.mad.lab5.db.Reservation
import it.polito.mad.lab5.rent.RentViewModel
import it.polito.mad.lab5.rent.ReservationDialog
import it.polito.mad.lab5.rent.ReservationList
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun Rent(vm: RentViewModel) {
    val context = LocalContext.current

    val sportsList = vm.sportsList1.toList()
    val selectedSport = vm.selectedSport.value

    val playgroundsList = vm.playgroundsList.toList()
    val selectedPlayground = vm.selectedPlayground.value

    val fullDates = vm.fullDates
    val selectedDate = vm.selectedDate.value

    val freeSlots = vm.freeSlots.toList()
    val selectedTimeSlot = vm.selectedTimeSlot.value

    val customRequest = vm.customRequest.value

    var expandedSport by remember { mutableStateOf(false) }
    var expandedField by remember { mutableStateOf(false) }
    var showFieldsDropDown  by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Rent a playing field", textAlign = TextAlign.Center)},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        },
        content = {it ->
            LazyColumn(
                modifier = Modifier.padding(it)
            ) {
                item {
                    //Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Choose your sport:", modifier = Modifier.padding(16.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedSport,
                        onExpandedChange = {
                            expandedSport = !expandedSport
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedSport,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSport) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        DropdownMenu(
                            expanded = expandedSport,
                            onDismissRequest = { expandedSport = false; vm.selectedDate.value = null },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)
                        ) {

                            sportsList.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = item, modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 16.dp, end = 16.dp)
                                        )
                                    },
                                    onClick = {
                                        vm.selectedSport.value = item
                                        expandedSport = false
                                        showFieldsDropDown = true
                                        vm.loadPlaygrounds()
                                        vm.selectedPlayground.value = "Playground"
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    if (showFieldsDropDown) {
                        if (selectedSport.isNotEmpty()) {
                            Text(text = "Select the playground:", modifier = Modifier.padding(16.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedField,
                                onExpandedChange = {
                                    expandedField = !expandedField
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(/*top = 16.dp, */start = 16.dp, end = 16.dp)
                            ) {
                                OutlinedTextField(
                                    value = selectedPlayground,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expandedField
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )

                                DropdownMenu(
                                    expanded = expandedField,
                                    onDismissRequest = {
                                        expandedField = false; vm.selectedDate.value = null
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp)
                                ) {

                                    playgroundsList.forEach { item ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = item,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(start = 16.dp, end = 16.dp)
                                                )
                                            },
                                            onClick = {
                                                vm.selectedPlayground.value = item
                                                expandedField = false
                                                vm.selectedDate.value = Date()
                                                vm.loadFullDates()
                                                vm.loadFreeSlots()
                                                //Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    if (selectedSport != "Sport" && selectedPlayground != "Playground") {

                        //val formatter = SimpleDateFormat("yyyy-MM-dd")
                        //val date = Date()

                        //val fullDates by viewModel.getFullDates(selectedField).observeAsState(initial = emptyList())
                        Text(text = "Select the date:", modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp))


                        //vm.selectedDate.value = Date()//LocalDate.now().toDate()
                        MyCalendar(
                            selectedDate = vm.selectedDate.value!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                            setSelectedDate = { date: LocalDate? ->
                                vm.selectedDate.value = date.toDate()
                                Log.d(TAG, "Selected date: ${vm.selectedDate.value} and $selectedDate")
                                vm.loadFreeSlots()
                                              },
                            isColored = { //it: CalendarDay ->
                                var found = false
                                for (d in fullDates) {
                                    if (d == Date.from(it.date.atStartOfDay(ZoneOffset.systemDefault()).toInstant())) {
                                        found = true
                                        break
                                    }
                                }
                                found},
                            backgroundColor = Color(0xFFe06666)
                        )
                    }
                }
                if (selectedSport != "Sport" && selectedPlayground != "Field" && selectedDate != null) {
                    item {
                        //if (selectedDate != null) {
                            Text(text = "Choose the hour:", modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp))
                            //val fasceLibere by viewModel.getFasceOrarieLibere(selectedField, date)
                                //.observeAsState(initial = emptyList())
                            if(freeSlots.isNotEmpty()) {
                                ReservationList(data = freeSlots) { chosenTimeSlot ->
                                    showDialog = true
                                    vm.selectedTimeSlot.value = chosenTimeSlot
                                }
                            }
                            else{
                                Text(text = "No slots available", modifier = Modifier.fillMaxWidth().padding(16.dp), textAlign = TextAlign.Center)
                            }
                        //}


                        if (showDialog) {
                            ReservationDialog(
                                onDismiss = {
                                    showDialog = false
                                    vm.customRequest.value = ""
                                },
                                onConfirm = { /*sport, field, date, timeSlot, customRequest -> */
                                    runBlocking {
                                        vm.saveReservation(
                                            /*Reservation(
                                            "",
                                            selectedDate.toDate(),
                                            selectedDate.toDate().time.toString(),
                                            sport,
                                            timeSlot?.oraInizio?.toInt() ?: 0,
                                            timeSlot?.oraFine?.toInt() ?: 0,
                                            field,
                                            customRequest)*/
                                        )
                                    }
                                    Toast.makeText(context, "Reservation saved", Toast.LENGTH_LONG)
                                        .show()
                                    showDialog = false
                                    vm.customRequest.value = ""
                                },
                                sport = selectedSport,
                                field = selectedPlayground,
                                date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                                timeSlot = selectedTimeSlot,
                                customRequest = customRequest
                            ) { vm.customRequest.value = it }
                        }
                    }
                }

            }
        }
    )

}

fun LocalDate?.toDate(): Date {
    return Date.from(this!!.atStartOfDay(ZoneOffset.systemDefault()).toInstant())
}



