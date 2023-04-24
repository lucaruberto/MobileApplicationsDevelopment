package it.polito.mad.lab2

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import it.polito.mad.lab2.databinding.FragmentShowReservationsBinding

class SearchPlayground: Fragment(R.layout.fragment_search_playground) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var  dropmenu : AutoCompleteTextView = view.findViewById(R.id.dropdown_sports)
    //  val  db = ReservationDatabase.getDatabase(view.context);
        val lista= listOf( "uno","due","tre")
        val arrayAdapter = ArrayAdapter(view.context,R.layout.list_item,lista);
        dropmenu.setAdapter(arrayAdapter)
    }
}