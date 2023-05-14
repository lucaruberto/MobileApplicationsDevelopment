package it.polito.mad.lab4.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.polito.mad.lab4.db.Sports

@Composable
fun SportCard(
    sport: Sports,
    level: Int,
    onLevelChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    setSelectedSport: (List<Sports>) -> Unit,
    selectedSport: List<Sports>,
    add: Boolean,
    selectedSportLevel: List<SportList>,
    setSelectedSportLevel: (List<SportList>) -> Unit
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
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(.60f)
                    )
                    if(add) {
                        Box(modifier = Modifier.weight(.40f)) {
                            Text(
                                text = testo,
                                modifier = Modifier.clickable { setExpandable(true) },
                                style = MaterialTheme.typography.bodySmall
                            );
                            DropdownMenu(
                                expanded = expandable,
                                onDismissRequest = { setExpandable(false) },
                                modifier = Modifier.width(30.dp)
                            ) {
                                (1..5).forEach { level ->
                                    DropdownMenuItem(
                                        text = { Text(text = "$level") }, onClick = {
                                            setTesto("$level")
                                            setExpandable(false)
                                        }, enabled = true
                                    )
                                }
                            }
                        }
                    }
                    else
                    {
                        println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC")
                        var livello= selectedSportLevel.first { it.sportname === sport.discipline }.level
                        println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD + $livello")

                        Text(text ="$livello" ,style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(.60f) )
                    }



                }
            }
            Column(modifier = Modifier.weight(0.24f)) {
                Button(modifier = Modifier
                    .padding(top = 40.dp)
                    .size(40.dp), onClick = {
                    if(add){
                        if(testo=="None"){

                            Toast.makeText(context,"Please Select a Skill Level before Add", Toast.LENGTH_LONG).show()
                        }
                        else{


                            setSelectedSport(selectedSport.plus(sport))
                            println("size di selected = ${selectedSport.size}")

                            val x= SportList(sportname = sport.discipline,level = testo.toInt())
                            val y= selectedSportLevel.plus(x)
                            println("size di y = ${y.size}")
                            setSelectedSportLevel(y)
                            setSelectedSport(listOf<Sports>(Sports(discipline = "Calcio", id = 2)))
                            println("size della lista aggiornata = ${selectedSportLevel.size}")
                            setTesto("None")
                        }


                    }
                    else
                    { setSelectedSport(selectedSport.filter { it.discipline!=sport.discipline })}
                    setSelectedSportLevel(selectedSportLevel.filter { it.sportname!=sport.discipline })
                }
                )
                {
                    if(add)
                        Text(text = "+")
                    else
                        Text(text = "-")
                }
            }
        }

    }
}
