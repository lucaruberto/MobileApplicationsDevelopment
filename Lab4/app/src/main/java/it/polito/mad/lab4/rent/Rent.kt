package it.polito.mad.lab4.rent

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.model.KalendarType
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.SelectableWeekCalendar
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import it.polito.mad.lab3.RentViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
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
    //val time by viewModel.getFasceOrariLibere(selectedField, selectedDate).observeAsState(initial = emptyList())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        
        Text(text = "Rent your player court", fontSize = 30.sp, fontStyle = FontStyle.Normal)
        Spacer(modifier = Modifier.height(32.dp))
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
                        text = { Text(text = item, modifier = Modifier.fillMaxWidth())  },
                        onClick = {
                            selectedSport = item
                            expandedSport = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            showFieldsDropDown = true
                            selectedField = "Field"
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        if( showFieldsDropDown ) {
            if(selectedSport.isNotEmpty()) {
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
                                text = { Text(text = item, modifier = Modifier.fillMaxWidth()) },
                                onClick = {
                                    selectedField = item
                                    expandedField = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        val currentDate = remember { mutableStateOf<Date?>(null) }
        Kalendar(kalendarType = KalendarType.Firey, modifier = Modifier.fillMaxWidth(),
        onCurrentDayClick = {day, events -> selectedDate=day.localDate})

        //https://github.com/hi-manshu/Kalendar



        Card(onClick = { /*TODO*/ }) {
            Row(modifier =Modifier.fillMaxWidth()) {
               //Text( selectedDate)
            }
        }

    }


    @Composable
    fun CardDate(localDate: kotlinx.datetime.LocalDate) {
        Card{

        }
    }

}




