package it.polito.mad.myapplication1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class XMLActivity : AppCompatActivity() {
    private var counter = 0
    lateinit var tv: TextView
    lateinit var b: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xmlactivity)

        tv = findViewById(R.id.tv)
        b = findViewById(R.id.b)
        b.setOnClickListener {
            counter++
            tv.text = "$counter"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tv_text", tv.text.toString())
        outState.putInt("counter", counter)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        tv.text = savedInstanceState.getString("tv_text")
        counter = savedInstanceState.getInt("counter")
    }
}