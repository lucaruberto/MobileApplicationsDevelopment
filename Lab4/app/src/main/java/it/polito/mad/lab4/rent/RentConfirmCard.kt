package it.polito.mad.lab4.rent

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import it.polito.mad.lab4.db.FasciaOraria
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

@Composable
fun ReservationDialog(
    onDismiss: () -> Unit,
    onConfirm: suspend (
        sport: String,
        field: String,
        date: LocalDate?,
        timeSlot: FasciaOraria?,
        customRequest: String
    )-> Unit,
    sport: String,
    field: String,
    date: java.time.LocalDate?,
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
            Button(colors = ButtonDefaults.buttonColors(Color.Green),onClick = { runBlocking{ onConfirm(sport, field, date, timeSlot, customRequest) } }) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(colors = ButtonDefaults.buttonColors(Color.Red),onClick = onDismiss) {
                Text("No")
            }
        }
    )
}