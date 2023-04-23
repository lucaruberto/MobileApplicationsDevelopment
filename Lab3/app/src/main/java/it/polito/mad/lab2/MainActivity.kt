package it.polito.mad.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = (
                supportFragmentManager
                    .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                ).navController
        val bottom_navbar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        
        //bottom_navbar.
        /*

        val switch = findViewById<Switch>(R.id.switch1)
        switch.setOnCheckedChangeListener { compoundButton, b ->
            if(b) {
                navController.navigate(R.id.action_fragmentOne_to_fragmentTwo)
            }
            else{
                navController.navigate(R.id.fragmentOne)
            }
        }
        */

    }
}