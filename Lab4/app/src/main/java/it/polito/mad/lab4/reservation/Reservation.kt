package it.polito.mad.lab4.reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.model.KalendarType

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Reservation() {
    //val vm: RentViewModel = viewModel()
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Your reservations", fontSize = 30.sp, fontStyle = FontStyle.Normal)
        Kalendar(kalendarType = KalendarType.Firey, modifier = Modifier.fillMaxWidth())

        Card {
            //vm.getListSport().value
        }
    }

}

