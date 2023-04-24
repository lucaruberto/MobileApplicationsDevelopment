package it.polito.mad.lab2

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import it.polito.mad.lab2.databinding.FragmentShowReservationsBinding

class ShowReservations : Fragment(R.layout.fragment_show_reservations) {

    private lateinit var db:ReservationDatabase;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val calendarView: CalendarView=view.findViewById(R.id.reservations_calendar_view)
        //db=ReservationDatabase.getDatabase(view.context)




    }
}