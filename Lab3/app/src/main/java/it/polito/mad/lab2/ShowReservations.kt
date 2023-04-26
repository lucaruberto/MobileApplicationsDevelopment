package it.polito.mad.lab2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class ShowReservations : Fragment(R.layout.fragment_show_reservations) {

    private lateinit var db:GlobalDatabase;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val calendarView: CalendarView=view.findViewById(R.id.reservations_calendar_view)
        //db=ReservationDatabase.getDatabase(view.context)




    }
}