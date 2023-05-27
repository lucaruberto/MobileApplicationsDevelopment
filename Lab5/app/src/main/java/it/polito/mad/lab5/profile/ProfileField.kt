package it.polito.mad.lab5.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun ProfileField(hover: String, text: String, type: String, setText: (String)->Unit, editMode: Boolean) {
    Card(modifier = Modifier.padding(bottom = 8.dp), colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,)
    ) {
        val focusManager = LocalFocusManager.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = hover,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            if (editMode)
                TextField(
                    value = text,
                    onValueChange = { setText(it) },
                    modifier = Modifier
                        .weight(2f)
                        ,
                    singleLine = true,
                    keyboardOptions = when(type) {
                        "date" -> KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )
                        "email" -> KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                        "simple" -> KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        )
                        else -> KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        )
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {focusManager.moveFocus(FocusDirection.Down) },
                        onDone = {focusManager.clearFocus()}
                    ),
                )
            else {
                if (text === "")
                    Text(
                        text = "Not inserted yet",
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                else
                    Text(
                        text = text,
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                    )
            }
        }
    }
}




