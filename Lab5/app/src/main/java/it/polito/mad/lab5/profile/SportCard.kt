package it.polito.mad.lab5.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.polito.mad.lab5.db.UserSports

@Composable
fun SportCard(
    sportName: String,
    level: String,
    modifier: Modifier = Modifier,
    add: Boolean,
    selectedSports: SnapshotStateList<UserSports>
) {
    val context = LocalContext.current
    val (expandable,setExpandable) = remember {mutableStateOf(false)}
    val (testo, setTesto) = remember { mutableStateOf(level) }

    Card(
        modifier = modifier
            .height(100.dp)
            .width(150.dp)
            .clickable {
                if (add) {
                    if (testo == "None") {
                        Toast
                            .makeText(
                                context,
                                "Please Select a Skill Level before Add",
                                Toast.LENGTH_LONG
                            )
                            .show()
                    } else {
                        val x = UserSports(sportName = sportName, level = testo)
                        selectedSports.add(x)
                        setTesto("None")
                    }
                } else {
                    val sportFound = selectedSports.find { it.sportName == sportName }
                    selectedSports.remove(sportFound)
                }
            } ,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
        ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(0.70f)
            ) {
                    Text(
                        text = sportName,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "Ability Level:",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    if(add) {
                        Row(modifier = Modifier.weight(1f)) {
                            Text(
                                text = testo,
                                modifier = Modifier.clickable { setExpandable(true) },
                                style = MaterialTheme.typography.bodySmall
                            )
                            DropdownMenu(
                                expanded = expandable,
                                onDismissRequest = { setExpandable(false) },
                                modifier = Modifier.width(110.dp)
                            ) {
                                (listOf("Beginner","Intermediate","Advanced")).forEach { level ->
                                    DropdownMenuItem(
                                        text = { Text(text = level) }, onClick = {
                                            setTesto(level)
                                            setExpandable(false)
                                        }, enabled = true
                                    )
                                }
                            }
                        }

                    }
                    else
                    {
                        Text(text = level,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f) )
                    }
            }
            Column(modifier = Modifier
                .weight(0.30f)
                .align(CenterVertically)
                .padding(end = 8.dp)){
               if(add) {
                   Icon(
                       imageVector = Icons.Default.Add,
                       contentDescription = null,
                       modifier = Modifier.padding(8.dp)
                   )
               }
                else{
                   Icon(
                       imageVector = Icons.Default.Delete,
                       contentDescription = null,
                       modifier = Modifier.padding(8.dp)
                   )
               }

        }}
    }
}
