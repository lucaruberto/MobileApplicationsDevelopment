package com.example.mylab3


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import com.stacktips.view.utils.CalendarUtils
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize CustomCalendarView from layout
        val calendarView = findViewById<View>(R.id.calendar_view) as CustomCalendarView
        val selectedDateTv = findViewById<View>(R.id.selected_date) as TextView

        //Initialize calendar with date
        val currentCalendar: Calendar = Calendar.getInstance(Locale.getDefault())

        //Show Monday as first date of week
        calendarView.firstDayOfWeek = Calendar.MONDAY

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false)

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar)

        //Handling custom calendar events
        calendarView.setCalendarListener(object : CalendarListener {
            override fun onDateSelected(date: Date?) {
                if (!CalendarUtils.isPastDay(date)) {
                    val df = SimpleDateFormat("dd-MM-yyyy")
                    selectedDateTv.text = "Selected date is ${df.format(date)}"
                } else {
                    selectedDateTv.text = "Selected date is disabled!"
                }
            }
            override fun onMonthChanged(date: Date?) {
                val df = SimpleDateFormat("MM-yyyy")
                Toast.makeText(this@MainActivity, df.format(date!!), Toast.LENGTH_SHORT).show()
            }
        })

        //adding calendar day decorators
        val decorators: MutableList<DayDecorator> = ArrayList()
        decorators.add(DisabledColorDecorator())
        calendarView.decorators = decorators
        calendarView.refreshCalendar(currentCalendar)
    }

    private inner class DisabledColorDecorator : DayDecorator {
        override fun decorate(dayView: DayView) {
            if (CalendarUtils.isPastDay(dayView.date)) {
                //val color: Int = Color.parseColor("#a9afb9")
                dayView.setBackgroundColor(
                    ContextCompat.getColor(this@MainActivity,
                        R.color.grey))
            }
        }
    }
}
