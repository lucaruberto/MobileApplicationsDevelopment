package it.polito.mad.lab4.reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.mad.lab4.MyCalendar
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaInstant
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Reservation() {
    val vm: ShowReservationsViewModel = viewModel()
    val liveDates by vm.getLiveDates().observeAsState(initial = emptyList())
    val (selectedDate, setSelectedDate) = remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = Modifier
        .fillMaxWidth()
        /*.padding(16.dp)*/) {
        Text(
            text = "Your reservations",
            fontSize = 30.sp,
            fontStyle = FontStyle.Normal,
            modifier = Modifier.padding(16.dp)
        )
        MyCalendar(selectedDate, setSelectedDate) {
            var found = false
            for (d in liveDates) {
                if (d == Date.from(it.date.atStartOfDay(ZoneOffset.systemDefault()).toInstant())) {
                    found = true
                    break
                }
            }
            found
        }
        /*Kalendar(
            kalendarType = KalendarType.Firey,
            currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            modifier = Modifier.fillMaxWidth(),
            kalendarDayKonfig = KalendarDayKonfig(
                        size = 56.dp,
                        textSize = 16.sp,
                        textColor = Color.Green,
                        selectedTextColor = Color.Black
                    )

        )*/

        Card {
            //vm.getListSport().value
        }
    }

}

