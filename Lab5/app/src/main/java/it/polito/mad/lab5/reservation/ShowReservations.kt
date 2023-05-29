package it.polito.mad.lab5.reservation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.polito.mad.lab5.MyCalendar
import it.polito.mad.lab5.db.FasciaOraria
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Reservation(vm: ShowReservationsViewModel) {
    val reservations = vm.reservations
    val dateList = reservations.map { it.date }
    val (selectedDate, setSelectedDate) = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    val selectedDateReservations = reservations
        .filter { it.date == Date.from(selectedDate?.atStartOfDay(ZoneOffset.systemDefault())?.toInstant() ) }
        //.map { FasciaOraria(it.oraInizio, it.oraFine) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Browse your reservations", textAlign = TextAlign.Center)},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        },
        content = { it ->
            /*LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
            ) {
                items(reservations) {
                    Text(text = it.toString())
                }
            }*/
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    MyCalendar(selectedDate, setSelectedDate, {
                        var found = false
                        for (d in dateList) {
                            if (d == Date.from(
                                    it.date.atStartOfDay(ZoneOffset.systemDefault()).toInstant()
                                )
                            ) {
                                found = true
                                break
                            }
                        }
                        found
                    }, Color.Gray)
                }

                items(items = selectedDateReservations.sortedBy { it.oraInizio }/*, key = { it.oraInizio}*/) {
                    //Text(text = "${it.oraInizio}:00 - ${it.oraFine}:00")
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column() {
                                Text(
                                    text = "${it.oraInizio}:00 - ${it.oraFine}:00",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                                Text(
                                    text = "${it.discipline} at ${it.playgroundName}",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 0.dp,
                                        bottom = 16.dp
                                    )
                                )
                                if (it.customRequest != "") {
                                    Text(
                                        text = "Custom requests: ${it.customRequest}",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 0.dp,
                                            bottom = 16.dp
                                        )
                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Button(onClick = {
                                    vm.deleteReservation(
                                        it.date,
                                        it.discipline,
                                        it.oraInizio,
                                        it.playgroundName
                                    )
                                }) {

                                    Icon(
                                        Icons.Rounded.Delete,
                                        contentDescription = "Delete Reservation"
                                    )

                                }
                            }
                        }

                    }
                }
            }
        }
    )

            /*
    val vm: ShowReservationsViewModel = viewModel()
    val liveDates by vm.getLiveDates().observeAsState(initial = emptyList())
    val (selectedDate, setSelectedDate) = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    val slots by vm.getReservationFromDate(Date.from(
        selectedDate?.atStartOfDay(ZoneOffset.systemDefault())?.toInstant()
    )).observeAsState(initial = emptyList())


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Browse your reservations", textAlign = TextAlign.Center)},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        },
        content = { it ->
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(it)) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item{
                    MyCalendar(selectedDate, setSelectedDate, {
                        var found = false
                        for (d in liveDates) {
                            if (d == Date.from(it.date.atStartOfDay(ZoneOffset.systemDefault()).toInstant())) {
                                found = true
                                break
                            }
                        }
                        found
                    }, Color.Gray)
                }

                items(items = slots.sortedBy { it.oraInizio }/*, key = { it.oraInizio}*/){
                    //Text(text = "${it.oraInizio}:00 - ${it.oraFine}:00")
                    Card(
                        shape = RoundedCornerShape(12. dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column() {
                                Text(
                                    text = "${it.oraInizio}:00 - ${it.oraFine}:00",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                                Text(
                                    text = "${it.discipline} at ${it.playgroundName}",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 0.dp,
                                        bottom = 16.dp)
                                )
                                if(it.customRequest != "") {
                                    Text(
                                        text = "Custom requests: ${it.customRequest}",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 0.dp,
                                            bottom = 16.dp)
                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Button(onClick = { vm.deleteReservation(
                                    it.id,
                                    it.date,
                                    it.time,
                                    it.discipline,
                                    it.oraInizio,
                                    it.oraFine,
                                    it.playgroundName,
                                    it.customRequest) }) {

                                    Icon(Icons.Rounded.Delete, contentDescription = "Delete Reservation")

                                }
                            }
                        }

                    }
                }
            }
        }
    )
    */
}

