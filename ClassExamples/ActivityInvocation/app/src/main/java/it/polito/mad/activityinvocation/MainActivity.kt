package it.polito.mad.activityinvocation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b = findViewById<Button>(R.id.button)
        b.setOnClickListener(){
            val i = Intent(this, SecondActivity::class.java)
            startActivity(i)
        }
    }
}