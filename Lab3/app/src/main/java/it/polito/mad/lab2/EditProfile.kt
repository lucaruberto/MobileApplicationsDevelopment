package it.polito.mad.lab2

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

class EditProfile: Fragment(R.layout.fragment_edit_profile) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.check -> {
                val navHost = (requireParentFragment().parentFragmentManager.findFragmentById(R.id.fragmentContainerView)) as NavHostFragment
                navHost.navController.navigate(R.id.showProfileNavGraph)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}