package it.polito.mad.lab4.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun ProfileField(hover: String, text: String, setText: (String)->Unit, editmode: Boolean) {
    var content by remember { mutableStateOf(text) }

    Card(modifier = Modifier.padding(bottom = 8.dp), colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,)
    ) {
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
            if (editmode)
                OutlinedTextField(
                    value = content,
                    onValueChange = {
                        new -> content = new
                        setText(content)
                    },
                    modifier = Modifier
                        .weight(2f)
                        .size(width = 200.dp, height = 30.dp),
                    singleLine = true,
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



