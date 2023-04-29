package it.polito.mad.myapplication1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData

class MainActivity : AppCompatActivity() {
    private var counter = MutableLiveData<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv)
        counter.observe(this) {
            tv.setText("$it")
        }
        val b = findViewById<Button>(R.id.b)
        b.setOnClickListener {
            counter.value = (counter.value ?: 0 ) + 1
        }
    }
}