package it.polito.mad.lab4.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileField(hover:String,text:String,setText : (String)->Unit,editmode: Boolean)
{
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(50.dp)
            .width(250.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = hover,
                modifier = Modifier
                    .padding(top = 15.dp, end = 8.dp)
                    .width(100.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
            if(editmode)
                TextField(
                    value = text,
                    onValueChange = { setText(it) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxSize()
                        .height(48.dp),
                    maxLines = 1,
                    singleLine = true,

                    )
            else
            {
                if(text==="")
                    Text(
                        text = "Not Inserted Yet",
                        modifier = Modifier.padding(top=15.dp,end = 8.dp),

                        )
                else
                    Text(
                        text = text,
                        modifier = Modifier.padding(top=15.dp,end = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                    )

            }
        }


    }


}
