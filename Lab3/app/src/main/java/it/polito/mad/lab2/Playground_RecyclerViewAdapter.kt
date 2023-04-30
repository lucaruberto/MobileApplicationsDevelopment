package it.polito.mad.lab2

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import it.polito.mad.lab2.db.GlobalDatabase
import it.polito.mad.lab2.db.Reservation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.concurrent.thread

class Playground_RecyclerViewAdapter(val data : List<ShowReservationModel>, val date: Date?, val dropmenu : String, val dropmenufields: String,
                                     private val vm:SearchPlaygroundViewModel) : RecyclerView.Adapter <Playground_RecyclerViewAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): Playground_RecyclerViewAdapter.MyViewHolder {

        val v= LayoutInflater.from(parent.context).inflate(R.layout.cardview_reservation,parent,false)
        return MyViewHolder(v,vm)
    }

    override fun onBindViewHolder(
        holder: Playground_RecyclerViewAdapter.MyViewHolder,
        position: Int) {
        val rs= data[position];
        holder.bind(rs, holder, date!!,  dropmenu, dropmenufields,vm);

    }

    override fun getItemCount(): Int {
        return data.size
        }

    class  MyViewHolder(v: View,vm:SearchPlaygroundViewModel) : RecyclerView.ViewHolder(v){
        val StarHour: TextView = v.findViewById(R.id.Orainizio)
        val FinishHour: TextView = v.findViewById(R.id.Orafine)
        val CardView : CardView = v.findViewById(R.id.cardview)


        fun bind(rs: ShowReservationModel, holder: MyViewHolder, date: Date?, dropmenu: String, dropmenufields: String,vm: SearchPlaygroundViewModel) {
            StarHour.text = rs.StartHour.toString();
            FinishHour.text = rs.FinishHour.toString();
            CardView.setOnClickListener {
                val message = "Are You sure?"
                showCustomDialogBox(holder.CardView.context, message, date!!, dropmenu, dropmenufields, rs.StartHour.toString(), rs.FinishHour.toString(),vm)
            }
        }

        @SuppressLint("ResourceAsColor")
        private fun showCustomDialogBox(context: Context, message: String?, date: Date?, dropmenu: String, dropmenufields: String, start: String, end: String,vm:SearchPlaygroundViewModel) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.reservation_popup)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val df = SimpleDateFormat("dd-MM-yyyy")
            val tvMessage :TextView = dialog.findViewById(R.id.tvMessage)
            val btnYes : Button = dialog.findViewById(R.id.btnYes)
            val btnNo : Button = dialog.findViewById(R.id.btnNo)
            val sport : TextView = dialog.findViewById(R.id.sportSelected)
            val playerCourt : TextView = dialog.findViewById(R.id.playerCourtSelected)
            val dateS : TextView = dialog.findViewById(R.id.dateSelected)
            val _start : TextView = dialog.findViewById(R.id.start)
            val _end : TextView = dialog.findViewById(R.id.end)
            tvMessage.text = message
            sport.text = dropmenu
            dateS.text = df.format(date).toString()
            _start.text = start
            _end.text = end
            println(sport.text)
            sport.setTextColor(R.color.black)
            playerCourt.text = dropmenufields

            btnYes.setOnClickListener {

                thread {
                        vm.saveReservation(
                            0,
                            date!!,
                            date.time.toString(),
                            dropmenu,
                            start.toInt(),
                            end.toInt(),
                            dropmenufields
                        )
                }
                Toast.makeText(context, "Reservation saved", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            btnNo.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}