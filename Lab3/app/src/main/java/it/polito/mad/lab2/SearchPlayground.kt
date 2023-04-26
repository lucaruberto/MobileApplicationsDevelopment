package it.polito.mad.lab2

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlin.math.log

class SearchPlayground: Fragment(R.layout.fragment_search_playground) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var  dropmenu : AutoCompleteTextView = view.findViewById(R.id.dropdown_sports)
        val  db = GlobalDatabase.getDatabase(this.requireContext());
        var listafinale :List<String> = listOf("");
        val valore = db.sportsDao().getAll().observe(viewLifecycleOwner, Observer {
            sports->dropmenu.setAdapter(ArrayAdapter(view.context,R.layout.list_item,sports));
        })

        
    }
}