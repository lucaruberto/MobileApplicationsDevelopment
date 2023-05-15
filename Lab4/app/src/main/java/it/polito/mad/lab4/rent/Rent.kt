import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.mad.lab4.MyCalendar
import it.polito.mad.lab4.db.FasciaOraria
import it.polito.mad.lab4.rent.RentViewModel
import it.polito.mad.lab4.rent.ReservationDialog
import it.polito.mad.lab4.rent.ReservationList
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaInstant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Rent() {
    val viewModel: RentViewModel = viewModel()
    val context = LocalContext.current
    val sportsList by viewModel.sportsList.observeAsState(initial = emptyList())
    var selectedSport by remember { mutableStateOf("Sport") }
    val fields by viewModel.getPlaygroundsbyName(selectedSport).observeAsState(initial = emptyList())
    var selectedField by remember { mutableStateOf("Field") }
    var expandedSport by remember { mutableStateOf(false) }
    var expandedField by remember { mutableStateOf(false) }
    var showFieldsDropDown  by remember { mutableStateOf(false) }
    val (selectedDate, setSelectedDate) = remember { mutableStateOf<LocalDate?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedTimeSlot by remember { mutableStateOf<FasciaOraria?>(null) }
    var customRequest by remember { mutableStateOf("") }
    LazyColumn(
        /*modifier = Modifier
            .padding(16.dp)*/
    ) {
        item {
            Text(text = "Rent your player court", fontSize = 30.sp, fontStyle = FontStyle.Normal,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                )
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
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
                    onDismissRequest = { expandedSport = false; setSelectedDate(null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {

                    sportsList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item, modifier = Modifier.fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)) },
                            onClick = {
                                selectedSport = item
                                expandedSport = false
                                showFieldsDropDown = true
                                selectedField = "Field"
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            if (showFieldsDropDown) {
                if (selectedSport.isNotEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = expandedField,
                        onExpandedChange = {
                            expandedField = !expandedField
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedField,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedField) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        DropdownMenu(
                            expanded = expandedField,
                            onDismissRequest = { expandedField = false; setSelectedDate(null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)
                        ) {

                            fields.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = item,
                                            modifier = Modifier.fillMaxWidth()
                                                .padding(start = 16.dp, end = 16.dp)
                                        )
                                    },
                                    onClick = {
                                        selectedField = item
                                        expandedField = false
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
            Spacer(modifier = Modifier.height(8.dp))
            if (selectedSport != "Sport" && selectedField != "Field") {
                /*com.himanshoe.kalendar.Kalendar(currentDay = Clock.System.todayIn(
                    TimeZone.currentSystemDefault()
                ), kalendarType = KalendarType.Firey,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    onDayClick = { day, events ->
                        val currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault())
                        if (selectedSport != "Sport" && selectedField != "Field" && day.compareTo(
                                currentDay
                            ) >= 0
                        ) {
                            selectedDate = day
                        } else {
                            Toast.makeText(
                                context,
                                "You can't select a past date",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })*/
                    MyCalendar(selectedDate = selectedDate, setSelectedDate = setSelectedDate, isColored = {false})
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        if (selectedSport != "Sport" && selectedField != "Field" && selectedDate != null) {
            item {
                val date = selectedDate?.toDate()

                if (date != null) {
                    val fasceLibere by viewModel.getFasceOrariLibere(selectedField, date)
                        .observeAsState(initial = emptyList())
                    ReservationList(data = fasceLibere) { chosenTimeSlot ->
                        showDialog = true
                        selectedTimeSlot = chosenTimeSlot
                    }
                }


                if (showDialog) {
                    ReservationDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { sport, field, date, timeSlot, customRequest ->
                            runBlocking {
                                viewModel.saveReservation(
                                    0,
                                    selectedDate.toDate(),
                                    selectedDate.toDate().time.toString(),
                                    sport,
                                    timeSlot?.oraInizio?.toInt() ?: 0,
                                    timeSlot?.oraFine?.toInt() ?: 0,
                                    field,
                                    customRequest
                                )
                            }
                            Toast.makeText(context, "Reservation saved", Toast.LENGTH_LONG).show()
                            showDialog = false
                        },
                        sport = selectedSport,
                        field = selectedField,
                        date = selectedDate,
                        timeSlot = selectedTimeSlot,
                        customRequest = customRequest
                    ) { customRequest = it }
                }
            }
        }
    }
}

fun LocalDate?.toDate(): Date {
    return Date.from(this!!.atStartOfDay(ZoneOffset.systemDefault()).toInstant())
}



