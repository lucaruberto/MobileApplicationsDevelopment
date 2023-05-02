package it.polito.mad.lab3

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ShowReservations : Fragment(R.layout.fragment_show_reservations) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendarView = requireView().findViewById<CustomCalendarView>(R.id.reservations_calendar_view)
        val currentCalendar: Calendar = Calendar.getInstance(Locale.getDefault())
        val recycle : RecyclerView = view.findViewById(R.id.reservations_recycle_view)
        val vm : ShowReservationsViewModel = ViewModelProvider(this)[ShowReservationsViewModel::class.java]

        recycle.visibility=View.GONE

        calendarView.setCalendarListener(object : CalendarListener {
               override fun onDateSelected(date: Date?) {
                   if (date != null) {
                       vm.getReservationFromDate(date).observe(viewLifecycleOwner){ x->
                           recycle.visibility = View.VISIBLE
                           val adapter= vm.getRecyclerAdapter(x,date)
                           recycle.adapter=adapter
                           recycle.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                       }
                   }
               }
               override fun onMonthChanged(date: Date?) {
                recycle.visibility=View.GONE
               }
           })

        vm.getLiveDates().observe(viewLifecycleOwner) { it ->
            class HasReservationDecorator : DayDecorator {
                override fun decorate(dayView: DayView) {
                    if(it.map { SimpleDateFormat("dd-MM-yyyy", Locale.ITALY).format(it) }
                            .contains(SimpleDateFormat("dd-MM-yyyy", Locale.ITALY).format(dayView.date))){
                        dayView.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.green))
                    }
                }
            }
            val decorators: MutableList<DayDecorator> = ArrayList()
            decorators.add(HasReservationDecorator())
            calendarView.decorators = decorators
            calendarView.refreshCalendar(currentCalendar)
        }
    }
}