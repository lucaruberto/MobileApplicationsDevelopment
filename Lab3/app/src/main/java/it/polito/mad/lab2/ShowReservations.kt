package it.polito.mad.lab2

import android.R.string
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import it.polito.mad.lab2.db.GlobalDatabase
import it.polito.mad.lab2.db.Reservation
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ShowReservations : Fragment(R.layout.fragment_show_reservations) {
    private lateinit var db: GlobalDatabase
    private lateinit var liveDates: LiveData<List<Date>>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db= GlobalDatabase.getDatabase(this.requireContext())
        val calendarView = requireView().findViewById<CustomCalendarView>(R.id.reservations_calendar_view)
        val recycle : RecyclerView = view.findViewById(R.id.reservations_recycle_view);
        recycle.visibility=View.GONE

        calendarView.setCalendarListener(object : CalendarListener {
               override fun onDateSelected(date: Date?) {
                   db.reservationDao().loadAllByDate(date!!).observe(viewLifecycleOwner){ x->
                       recycle.visibility = View.VISIBLE
                       val adapter= Reservation_RecyclerViewAdapter(x.map { y -> it.polito.mad.lab2.ShowReservationModel(y.discipline,y.playgroundName,y.date,y.oraInizio,y.oraFine) },date)
                       recycle.adapter=adapter
                       recycle.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

                   }

               }

               override fun onMonthChanged(date: Date?) {

               }
           })


        //Initialize calendar with date
        val currentCalendar: Calendar = Calendar.getInstance(Locale.getDefault())

        liveDates = db.reservationDao().loadAllDate()

        liveDates.observe(viewLifecycleOwner) {
            class HasReservationDecorator : DayDecorator {
                override fun decorate(dayView: DayView) {
                    //if(liveDates.value?.contains(dayView.date) == true){
                    if(it.map { SimpleDateFormat("dd-MM-yyyy").format(it) }.contains(SimpleDateFormat("dd-MM-yyyy").format(dayView.date))){
                    //if(true){
                        dayView.setBackgroundColor(
                            ContextCompat.getColor(requireContext(), R.color.red))
                    }
                }
            }
            val decorators: MutableList<DayDecorator> = ArrayList()
            decorators.add(HasReservationDecorator())
            calendarView.decorators = decorators

            calendarView.refreshCalendar(currentCalendar)
        }
        /*
        val addBtn = requireView().findViewById<FloatingActionButton>(R.id.addReservationButton)
        addBtn.setOnClickListener{
            val db = GlobalDatabase.getDatabase(this.requireContext())

            val t = Thread {
                db.reservationDao().save(
                    Reservation(
                        date = Calendar.getInstance(Locale.getDefault()).time,
                        time = "18:00",
                        discipline = "FootBall",

                        oraInizio = 18,
                        oraFine = 19,
                        playgroundName = "Ruffini"

                    )
                )
                liveDates = db.reservationDao().loadAllDate()
            }
            t.start()
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show() */

    }


}