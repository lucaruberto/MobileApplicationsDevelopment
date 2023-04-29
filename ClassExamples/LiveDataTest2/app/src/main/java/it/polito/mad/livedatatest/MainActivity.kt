package it.polito.mad.livedatatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels


class MainActivity : AppCompatActivity() {
    val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv)
        val b1 = findViewById<Button>(R.id.b1)
        val b2 = findViewById<Button>(R.id.b2)
        val c = findViewById<Button>(R.id.clear)

        vm.counter.observe(this){
            if (it == null) tv.setText(R.string.hello)
            else tv.setText("$it")
            b2.isEnabled = it>0
        }

        b1.setOnClickListener { vm.increment() }
        b2.setOnClickListener { vm.decrement() }
        c.setOnClickListener { vm.clear() }
    }
}