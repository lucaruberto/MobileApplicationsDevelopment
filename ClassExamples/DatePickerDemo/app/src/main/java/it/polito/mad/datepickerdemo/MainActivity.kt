package it.polito.mad.datepickerdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import it.polito.mad.datepickerdemo.ui.theme.DatePickerDemoTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatePickerDemoTheme {
                var pickedDate by remember {
                    mutableStateOf(LocalDate.now())
                }

                val formattedDate by remember {
                    derivedStateOf {
                        DateTimeFormatter
                            .ofPattern("dd/MM/yyyy")
                            .format(pickedDate)
                    }
                }


                val dateDialogState = rememberMaterialDialogState()

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        dateDialogState.show()
                    }) {
                        Text(text = "Pick date")
                    }
                    Text(text = formattedDate)

                }
                MaterialDialog(
                    dialogState = dateDialogState,
                    shape = RoundedCornerShape(16.dp),
                    buttons = {
                        positiveButton(
                            text = "Ok",
                            textStyle = TextStyle(color = Color.Black)
                        )
                        negativeButton(
                            text = "Cancel",
                            textStyle = TextStyle(color = Color.LightGray)
                        )
                    }
                ) {
                    datepicker(
                        initialDate = pickedDate,
                        title = "Pick a date",
                        colors = DatePickerDefaults.colors(
                            headerBackgroundColor = MaterialTheme.colorScheme.primary,
                            headerTextColor = Color.Black,
                            dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                        ),
                    ) {
                        pickedDate = it
                    }
                }
            }
        }
    }
}