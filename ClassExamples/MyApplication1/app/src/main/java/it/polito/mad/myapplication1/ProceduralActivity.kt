package it.polito.mad.myapplication1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class ProceduralActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ALMOST NEVER DONE IN THIS WAY!!
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = Gravity.CENTER_HORIZONTAL

        val tv = TextView(this)
        tv.text = "Hello, Android!"
        tv.textSize = 40.0f

        val b = Button(this)
        b.text = "Press me!"

        linearLayout.addView(tv)
        linearLayout.addView(b)

        setContentView(linearLayout)

    }
}