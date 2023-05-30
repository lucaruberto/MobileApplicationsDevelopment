package it.polito.mad.cardwithimageexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.polito.mad.cardwithimageexample.ui.theme.CardWithImageExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardWithImageExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        item{
                            MyCardWithImage(text = "ciao")
                        }
                        item {
                            MyCardWithImage(text = "sonooooo")
                        }
                        item {
                            MyCardWithImage(text = "andrea")
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun MyCardWithImage(text: String) {
    //val context = LocalContext.current
    val (showContent, setShowContent) = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterVertically)
                .background(MaterialTheme.colorScheme.surface),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            //Toast.makeText(context, "Clicked me: $text", Toast.LENGTH_SHORT).show()
                                   setShowContent(!showContent)
                            },
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = CenterVertically,
                            //horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Image(
                                modifier = Modifier
                                    .weight(0.4f)
                                    .clip(shape = CircleShape)
                                    .size(62.dp),
                                painter = painterResource(R.drawable.question),
                                contentDescription = null,
                            )
                            //Spacer(modifier = Modifier.width(64.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth().weight(0.6f),
                                text = text,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )

                        }
                        if(showContent)
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = CenterVertically ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth().weight(1f),
                                    text = "Not hidden anymore!",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                    }
                }
            }
        )
    }
}

