package it.polito.mad.lab2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import it.polito.mad.lab2.db.GlobalDatabase
import java.util.*




class SearchPlayground: Fragment(R.layout.fragment_search_playground) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dropmenu: AutoCompleteTextView = view.findViewById(R.id.dropdown_sports)
        var dropmenufields : AutoCompleteTextView = view.findViewById(R.id.dropdown_playgrounds)
        var playgroundText : TextInputLayout = view.findViewById(R.id.dropdown_playgrounds_parent)
        val db = GlobalDatabase.getDatabase(this.requireContext());

        val sportslist = db.sportsDao().getAll().observe(viewLifecycleOwner, Observer { sports ->
            dropmenu.setAdapter(ArrayAdapter(view.context, R.layout.list_item, sports));

            var selectedsport:String=""

            dropmenu.onItemClickListener = object : AdapterView.OnItemSelectedListener,
                OnItemClickListener {
                override fun equals(other: Any?): Boolean {
                    return super.equals(other)
                }

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long){
                    selectedsport=sports.get(p2)
                    val fieldslist = db.playgroundsDao().getPlayGroundsbySportName(selectedsport).observe(viewLifecycleOwner,
                        Observer {
                                fields ->
                            dropmenufields.setAdapter(ArrayAdapter(view.context,R.layout.list_item,fields))
                        })
                    dropmenufields.clearListSelection()
                    dropmenufields.clearComposingText()
                    dropmenufields.setText("")

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {


                }
            }



        })






        val calendarView = view.findViewById<CustomCalendarView>(R.id.playground_calendar_view)
        var currentCalendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendarView.firstDayOfWeek = Calendar.MONDAY

        val rs : List<ReservationModel> = listOf(ReservationModel("8","9"), ReservationModel("9","10"));

        calendarView.setCalendarListener(object : CalendarListener {
            val selectedValue = dropmenu.text.toString()
            override fun onDateSelected(date: Date?) {
                Toast.makeText(requireContext(), date.toString() , Toast.LENGTH_SHORT).show()
                val recycle : RecyclerView = view.findViewById(R.id.playground_recycle_view);
                val adapter = Playground_RecyclerViewAdapter(rs);
                recycle.adapter=adapter
                recycle.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            }
            override fun onMonthChanged(date: Date?) {
            }
        })



    }
}