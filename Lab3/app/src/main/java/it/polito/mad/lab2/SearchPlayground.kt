package it.polito.mad.lab2

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import java.util.*


class SearchPlayground: Fragment(R.layout.fragment_search_playground) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var sportsdropdownmenu: AutoCompleteTextView = view.findViewById(R.id.dropdown_sports)
        var fieldsdropdownmenu : AutoCompleteTextView = view.findViewById(R.id.dropdown_playgrounds)
        var playgroundText : TextInputLayout = view.findViewById(R.id.dropdown_playgrounds_parent)
        val calendarView = view.findViewById<CustomCalendarView>(R.id.playground_calendar_view)

        val recycle : RecyclerView = view.findViewById(R.id.playground_recycle_view)
        val vm : SearchPlaygroundViewModel = ViewModelProvider(this)[SearchPlaygroundViewModel::class.java]
        var selectedsport: String


        recycle.visibility=View.GONE



        vm.getFasceOrari().observe(viewLifecycleOwner) { hoursfasce ->
            calendarView.setCalendarListener(object : CalendarListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDateSelected(date: Date?) {
                    recycle.visibility = View.VISIBLE
                    val adapter = vm.getRecyclerAdapter(
                        hoursfasce,
                        sportsdropdownmenu.text.toString(),
                        fieldsdropdownmenu.text.toString(),
                        date!!,
                        date.time.toString()
                    )
                    recycle.adapter = adapter
                    recycle.layoutManager =
                         GridLayoutManager(context, 2)
                }

                override fun onMonthChanged(date: Date?) {
                    recycle.visibility = View.GONE
                }
            })
        }


        vm.getListSport().observe(viewLifecycleOwner) { sports ->
            sportsdropdownmenu.setAdapter(ArrayAdapter(view.context, R.layout.list_item, sports))
            sportsdropdownmenu.onItemClickListener = object : AdapterView.OnItemSelectedListener,
                OnItemClickListener {
                override fun equals(other: Any?): Boolean {
                    return super.equals(other)
                }

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedsport = sports[p2]
                    vm.getPlaygroundsbyName(selectedsport).observe(
                        viewLifecycleOwner
                    ) { fields ->
                        fieldsdropdownmenu.setAdapter(
                            ArrayAdapter(
                                view.context,
                                R.layout.list_item,
                                fields
                            )
                        )
                        fieldsdropdownmenu.onItemClickListener =
                            object : AdapterView.OnItemSelectedListener,
                                OnItemClickListener {
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {}
                                override fun onItemClick(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    recycle.visibility = View.GONE
                                    calendarView.visibility = View.VISIBLE
                                }
                            }
                    }
                    fieldsdropdownmenu.setText("")
                    playgroundText.visibility = View.VISIBLE;
                    fieldsdropdownmenu.visibility = View.VISIBLE;
                    calendarView.visibility = View.GONE
                    recycle.visibility = View.GONE;

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {


                }
            }


        }

    }

}

