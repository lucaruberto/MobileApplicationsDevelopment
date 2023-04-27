package it.polito.mad.lab2

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.stacktips.view.CustomCalendarView
import it.polito.mad.lab2.db.GlobalDatabase
import java.util.Calendar
import java.util.Locale

class SearchPlayground: Fragment(R.layout.fragment_search_playground) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dropmenu: AutoCompleteTextView = view.findViewById(R.id.dropdown_sports)
        val db = GlobalDatabase.getDatabase(this.requireContext());
        val sportslist = db.sportsDao().getAll().observe(viewLifecycleOwner, Observer { sports ->
            dropmenu.setAdapter(ArrayAdapter(view.context, R.layout.list_item, sports));
        })

        val calendarView = view.findViewById<CustomCalendarView>(R.id.playground_calendar_view)
        var currentCalendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendarView.firstDayOfWeek = Calendar.MONDAY
        calendarView.setOnClickListener {
            Toast.makeText(this.requireContext(),"Hai cliccato!",Toast.LENGTH_SHORT).show()
        }

    }
}