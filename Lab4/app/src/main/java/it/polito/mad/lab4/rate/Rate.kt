package it.polito.mad.lab4.rate

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.mad.lab4.db.Rating


@Composable
fun Rate() {
    val vmRatings: RateViewModel = viewModel()
    val ratings by vmRatings.getAllReviews().observeAsState()
    val fieldsNotRated by vmRatings.getFieldsNotRated().observeAsState()
    val (readMode, setReadMode) = remember { mutableStateOf(true) }
    val (showForm, setShowForm) = remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedField by remember { mutableStateOf("Select field") }



    if(readMode) {
        LazyColumn {
            ratings?.forEach {
                item {
                    ReviewComponent(
                        it.id,
                        it.fieldName,
                        it.reviewText,
                        vmRatings,
                        modifier = Modifier.padding(16.dp)
                    )
                    //Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { setReadMode(false) }) {
                        Text("Add review")
                    }
                }
            }
        }
    }
    else {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable(onClick = { expanded = true })
                    .background(Color.LightGray),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    selectedField, modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.padding(16.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                fieldsNotRated?.forEach { field -> DropdownMenuItem(
                    text = { Text(text = field, modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally))},
                    onClick = {
                        selectedField = field
                        expanded = false
                        setShowForm(true) },
                    modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (showForm)
                InsertReviewForm(modifier = Modifier.padding(24.dp), selectedField, vmRatings) { setReadMode(true) }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                        setReadMode(true)
                        setShowForm(false)
                        },
                        colors = ButtonDefaults
                            .buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                    Text("Back")
                }
            }
        }
    }
}



@Composable
fun ReviewComponent(reviewId: Int, fieldName: String, reviewText: String, vm: RateViewModel, modifier: Modifier) {
    val (showButtons, setShowButtons) = remember { mutableStateOf(false) }

    Column(
        modifier = modifier.then(
            Modifier
                .padding(start = 16.dp, end = 16.dp)
                .background(Color(0xFFF5F5F5))
        )
    ) {
        Text(
            text = fieldName,
            style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .clickable { setShowButtons(!showButtons) }
        )

        if (showButtons) {
            Text(
                text = reviewText,
                style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = { vm.deleteReview(Rating(reviewId, reviewText, fieldName)) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertReviewForm(modifier: Modifier, selectedField: String, vm: RateViewModel, onButtonClick: (Boolean) -> Unit) {
    val context = LocalContext.current
    var content by remember { mutableStateOf("") }

    Column(
        modifier = modifier.then(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
       Text("Insert your review", fontSize = 24.sp)
       Spacer(modifier = Modifier.height(16.dp))

       TextField(
           value = content,
           onValueChange = { content = it },
           label = { Text("Review...") },
           modifier = Modifier.fillMaxWidth()
       )
       Spacer(modifier = Modifier.height(16.dp))
       Row(
           horizontalArrangement = Arrangement.Center,
           modifier = Modifier.fillMaxWidth()
       ) {
           Button(onClick = {
               vm.saveReview( Rating(0,content,selectedField) )
               Toast.makeText(context, "Review saved", Toast.LENGTH_LONG).show()
               onButtonClick(true)
           }) {
               Text("Save")
           }
       }
   }
}
