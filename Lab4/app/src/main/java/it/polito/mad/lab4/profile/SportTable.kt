package it.polito.mad.lab4.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SportsTable(selectedSportsLevel: SnapshotStateList<SportList>) {

    MaterialTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.width(300.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Sports and Skill Levels",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                        color = Color.Red
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Sport",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Skill Level",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }

                    Divider(modifier = Modifier.height(1.dp), color = MaterialTheme.colorScheme.onSurface)

                    selectedSportsLevel.forEach { (sport, skillLevel) ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(Color.Gray),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(

                                    text = sport,
                                    modifier = Modifier.weight(1f),
                                    fontFamily = FontFamily.Serif,
                                    textAlign = TextAlign.Center

                                )

                                Text(
                                    text = skillLevel,
                                    modifier = Modifier.weight(1f),
                                    fontFamily = FontFamily.Serif,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Divider(
                                modifier = Modifier.padding(vertical = 6.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            )
                        }
                    }
                }
            }
        }
    }
}