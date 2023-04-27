package it.polito.mad.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var db : GlobalDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db= GlobalDatabase.getDatabase(applicationContext)

        val navController = (
                supportFragmentManager
                    .findFragmentById(R.id.mainFragmentContainerView) as NavHostFragment
                ).navController
        val bottomNavbar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        
        bottomNavbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navbar_menu_item_profile -> {
                    navController.navigate(R.id.showProfileNavGraph)
                }
                R.id.navbar_menu_item_reservation -> {

                    navController.navigate(R.id.showReservationsNavGraph)
                }
                R.id.navbar_menu_item_playground -> {
                    navController.navigate(R.id.searchPlaygroundNavGraph)
                }
            }
            true
        }

    /*    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val firstFragment=FirstFragment()
            val secondFragment=SecondFragment()
            val thirdFragment=ThirdFragment()

            setCurrentFragment(firstFragment)

            bottomNavigationView.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.home->setCurrentFragment(firstFragment)
                    R.id.person->setCurrentFragment(secondFragment)
                    R.id.settings->setCurrentFragment(thirdFragment)

                }
                true
            }

        }

        private fun setCurrentFragment(fragment:Fragment)=
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment,fragment)
                commit()
            }

    }*/

    }
}