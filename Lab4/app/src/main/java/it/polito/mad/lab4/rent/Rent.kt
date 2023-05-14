import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarType
//import com.himanshoe.kalendar.model.KalendarType
import it.polito.mad.lab4.db.FasciaOraria
import it.polito.mad.lab4.rent.RentViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.todayIn
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
    var selectedDate by remember { mutableStateOf<kotlinx.datetime.LocalDate?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedTimeSlot by remember { mutableStateOf<FasciaOraria?>(null) }
    var customRequest by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        item{
            Text(text = "Rent your player court", fontSize = 30.sp, fontStyle = FontStyle.Normal)
            Spacer(modifier = Modifier.height(32.dp))}

        item {
            ExposedDropdownMenuBox(
                expanded = expandedSport,
                onExpandedChange = {
                    expandedSport = !expandedSport
                },
                modifier = Modifier.fillMaxWidth()
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
                    onDismissRequest = { expandedSport = false },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    sportsList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item, modifier = Modifier.fillMaxWidth()) },
                            onClick = {
                                selectedSport = item
                                expandedSport = false
                                //Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
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
                            .padding(top = 16.dp)
                            .fillMaxWidth()
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
                            onDismissRequest = { expandedField = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            fields.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = item,
                                            modifier = Modifier.fillMaxWidth()
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
            val currentDate = remember { mutableStateOf<Date?>(null) }

            Kalendar(currentDay = Clock.System.todayIn(
                TimeZone.currentSystemDefault()
            ),kalendarType = KalendarType.Firey,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                onDayClick = { day, events -> selectedDate = day })
        }

            //https://github.com/hi-manshu/Kalendar
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
                    onConfirm = {
                        // Implement your logic for saving the reservation here
                        showDialog = false
                    },
                    sport = selectedSport,
                    field = selectedField,
                    date = selectedDate,
                    timeSlot = selectedTimeSlot,
                    customRequest = customRequest,
                    onCustomRequestChange = { customRequest = it }
                )
            }
        }
    }
}

fun LocalDate?.toDate(): Date {
    return Date.from(this!!.atStartOfDayIn(TimeZone.currentSystemDefault()).toJavaInstant())
}

@Composable
fun ReservationList(data : List<FasciaOraria>, onTimeSlotClick: (FasciaOraria) -> Unit) {
    val columns = 4
    val rows = data.size / columns + if (data.size % columns != 0) 1 else 0

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.height(200.dp)
    ) {
            items(data) { item ->
                ReservationCard(rent = item, onClick = { onTimeSlotClick(item) })
            }

    }


}

@Composable
fun ReservationCard(rent : FasciaOraria, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row() {
            Text("${rent.oraInizio}")
            Text("-${rent.oraFine}")
        }
    }
}
@Composable
fun ReservationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    sport: String,
    field: String,
    date: LocalDate?,
    timeSlot: FasciaOraria?,
    customRequest: String,
    onCustomRequestChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirm Reservation") },
        text = {
            Column {
                Text("Sport: $sport")
                Text("Field: $field")
                Text("Date: ${date.toString()}")
                timeSlot?.let {
                    Text("Time: ${it.oraInizio} - ${it.oraFine}")
                }
                TextField(
                    value = customRequest,
                    onValueChange = onCustomRequestChange,
                    label = { Text("Custom Request") }
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("No")
            }
        }
    )
    }
