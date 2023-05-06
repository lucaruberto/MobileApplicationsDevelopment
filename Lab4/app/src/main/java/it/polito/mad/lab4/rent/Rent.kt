package it.polito.mad.lab4.rent

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.model.KalendarType
import it.polito.mad.lab3.RentViewModel
import androidx.compose.runtime.livedata.observeAsState
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Rent() {
    val viewModel: RentViewModel = viewModel()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        val context = LocalContext.current
        val sportsList by viewModel.sportsList.observeAsState(initial = emptyList())
        var selectedSport by remember { mutableStateOf("Sport") }
        //val fields = if (selectedSport.isNotEmpty()) vm.getPlaygroundsbyName(selectedSport).value else listOf()
        var selectedField by remember { mutableStateOf("Field") }
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.padding(bottom = 50.dp).fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedSport,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
                ) {

                sportsList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item, modifier = Modifier.fillMaxWidth())  },
                        onClick = {
                            selectedSport = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Kalendar(kalendarType = KalendarType.Firey, modifier = Modifier.fillMaxWidth())
    }
}
