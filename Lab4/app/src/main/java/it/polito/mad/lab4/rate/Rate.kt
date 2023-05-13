package it.polito.mad.lab4.rate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Rate() {
    Column(modifier = Modifier.padding(24.dp)) {
        ReviewComponent(text = "Buongiorno")
        Spacer(modifier = Modifier.height(16.dp))
        ReviewComponent(text = "Prova")
    }
}

@Composable
fun ReviewComponent(text: String) {
    val (showButtons, setShowButtons) = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp).background(Color(0xFFF5F5F5))) {
        // Text Field
        Text(
            text = text,
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .clickable { setShowButtons(!showButtons) }

        )

        // Row with two buttons
        if (showButtons) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = { /* do something */ },
                    modifier = Modifier
                        .background(Color(0xFFFFF59D))
                        .size(48.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = { /* do something */ },
                    modifier = Modifier
                        .background(Color(0xFFFFCDD2))
                        .size(48.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}