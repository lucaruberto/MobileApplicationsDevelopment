package it.polito.mad.lab4.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.polito.mad.lab4.db.Sports

@Composable
fun SportCard(
    sport: Sports,
    level: String,
    onLevelChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedSport: MutableList<Sports>,
    add: Boolean,
    selectedSportLevel : MutableList<SportList>
) {
    val context = LocalContext.current
    var (expandable,setExpandable) = remember { mutableStateOf(false) }
    var (testo, setTesto) = remember {
        mutableStateOf("None")
    }
    Card(
        modifier = modifier
            .height(100.dp)
            .width(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),

        ) {
        Row() {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(0.76f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = sport.discipline,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Text(
                        text = "Ability Level:",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(.50f)
                    )
                    if(add) {
                        Box(modifier = Modifier.weight(.50f)) {
                            Text(
                                text = testo,
                                modifier = Modifier.clickable { setExpandable(true) },
                                style = MaterialTheme.typography.bodySmall
                            );
                            DropdownMenu(
                                expanded = expandable,
                                onDismissRequest = { setExpandable(false) },
                                modifier = Modifier.width(110.dp)
                            ) {
                                (listOf<String>("Beginner","Intermediate","Advanced")).forEach { level ->
                                    DropdownMenuItem(
                                        text = { Text(text = "$level") }, onClick = {
                                            setTesto("$level")
                                            onLevelChanged(level)
                                            setExpandable(false)
                                        }, enabled = true
                                    )
                                }
                            }
                        }
                    }
                    else
                    {
                        Text(text ="$level" ,style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(.50f) )
                    }



                }
            }
            Column(modifier = Modifier.weight(0.24f)) {
                Button(
                    onClick = {
                    if(add){
                        if(testo=="None"){

                            Toast.makeText(context,"Please Select a Skill Level before Add", Toast.LENGTH_LONG).show()
                        }
                        else{
                            selectedSport.add(sport)
                            val x= SportList(sportname = sport.discipline,level = testo)
                            selectedSportLevel.add(x)
                            setTesto("None")
                        }


                    }
                    else
                    { selectedSport.remove(sport)
                        val sportLevelToRemove = selectedSportLevel.firstOrNull { it.sportname == sport.discipline }
                        sportLevelToRemove?.let { selectedSportLevel.remove(it)} }
                }
                )
                {
                    if(add)
                        Text(text = "AAAAAAAAAA", color = Color.Black , modifier = Modifier.fillMaxWidth(), fontSize = 20.sp)
                    else
                        Text(text = "-")
                }
            }
        }

    }
}

fun addToSelectedSports(sport: Sports, testo: String, setSelectedSport: (List<Sports>) -> Unit, setSelectedSportLevel: (List<SportList>) -> Unit, selectedSportLevel: List<SportList>, selectedSport: List<Sports>) {
    val x = SportList(sportname = sport.discipline, level = testo)
    setSelectedSport(selectedSport.plus(sport));
    setSelectedSportLevel(selectedSportLevel.plus(x))

}