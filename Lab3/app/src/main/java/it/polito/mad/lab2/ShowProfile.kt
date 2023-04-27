package it.polito.mad.lab2

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

class ShowProfile: Fragment(R.layout.fragment_show_profile) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.pencil -> {/*(
                    (parentFragmentManager
                        .findFragmentById(R.id.fragmentContainerView)) as NavHostFragment
                    ).navController
                    .navigate(R.id.editProfileNavGraph)*/
                val navHost = (requireParentFragment().parentFragmentManager.findFragmentById(R.id.fragmentContainerView)) as NavHostFragment
                navHost.navController.navigate(R.id.editProfileNavGraph)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}