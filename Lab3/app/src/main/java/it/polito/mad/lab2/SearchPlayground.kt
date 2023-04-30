package it.polito.mad.lab2

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import it.polito.mad.lab2.db.GlobalDatabase
import it.polito.mad.lab2.db.Reservation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class SearchPlayground: Fragment(R.layout.fragment_search_playground) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dropmenu: AutoCompleteTextView = view.findViewById(R.id.dropdown_sports)
        var dropmenufields : AutoCompleteTextView = view.findViewById(R.id.dropdown_playgrounds)
        var playgroundText : TextInputLayout = view.findViewById(R.id.dropdown_playgrounds_parent)
        val calendarView = view.findViewById<CustomCalendarView>(R.id.playground_calendar_view)
        calendarView.firstDayOfWeek = Calendar.MONDAY
        var selectedsport:String=""
        var selectedplayground:String=""
        val recycle : RecyclerView = view.findViewById(R.id.playground_recycle_view);
        recycle.visibility=View.GONE
        val db = GlobalDatabase.getDatabase(this.requireContext());



        db.fasciaorariaDao().getAllFasciaOraria().observe(viewLifecycleOwner, Observer {
            lista->

                    calendarView.setCalendarListener(object : CalendarListener {
                    val selectedValue = dropmenu.text.toString()
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDateSelected(date: Date?) {
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val selectedDate:String=sdf.format(date);

                        recycle.visibility=View.VISIBLE
                        val adapter= Playground_RecyclerViewAdapter(lista.map { x->it.polito.mad.lab2.ShowReservationModel(dropmenu.text.toString(),dropmenufields.text.toString(),date,x.oraInizio,x.oraFine) }, date!!, dropmenu.text.toString(), dropmenufields.text.toString())
                        recycle.adapter=adapter
                        recycle.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

                    //DB INSERTION RESERVATION
                        lifecycleScope.launch(Dispatchers.IO){
                            db.reservationDao().save(Reservation(0,date,date.time.toString(), dropmenu.text.toString(),14,15, dropmenufields.text.toString()))
                        }
                    }
                    override fun onMonthChanged(date: Date?) {

                    }
            })
        })


        val sportslist = db.sportsDao().getAll().observe(viewLifecycleOwner, Observer { sports ->
            dropmenu.setAdapter(ArrayAdapter(view.context, R.layout.list_item, sports))
            dropmenu.onItemClickListener = object : AdapterView.OnItemSelectedListener,
                OnItemClickListener {
                override fun equals(other: Any?): Boolean {
                    return super.equals(other)
                }

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long){
                    selectedsport= sports[p2]
                    val fieldslist = db.playgroundsDao().getPlayGroundsbySportName(selectedsport).observe(viewLifecycleOwner,
                        Observer {
                                fields ->
                            dropmenufields.setAdapter(ArrayAdapter(view.context,R.layout.list_item,fields))
                            dropmenufields.onItemClickListener= object  :AdapterView.OnItemSelectedListener,
                            OnItemClickListener{
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {

                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    TODO("Not yet implemented")
                                }

                                override fun onItemClick(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    recycle.visibility=View.GONE
                                    selectedplayground=fields.get(p2);
                                    calendarView.visibility=View.VISIBLE
                                }

                            }

                        })
                    dropmenufields.setText("")
                    playgroundText.visibility=View.VISIBLE;
                    dropmenufields.visibility=View.VISIBLE;
                    calendarView.visibility=View.GONE
                    recycle.visibility=View.GONE;

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {


                }
            }



        })

    }

}

