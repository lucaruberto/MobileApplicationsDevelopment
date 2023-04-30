package it.polito.mad.lab2

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import it.polito.mad.lab2.db.GlobalDatabase
import java.util.Date

class Reservation_RecyclerViewAdapter(val data : List<ShowReservationModel>, val date: Date?, private val vm: ShowReservationsViewModel) : RecyclerView.Adapter <Reservation_RecyclerViewAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): MyViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.cardview_showreservation,parent,false)
        return MyViewHolder(v,vm)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int) {
        val srs= data[position];
        holder.bind(srs, holder, date!!,vm);
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class  MyViewHolder(v: View,vm : ShowReservationsViewModel) : RecyclerView.ViewHolder(v) {
        //Mostrare la data in qualche modo
        val CardView : CardView = v.findViewById(R.id.cardviewshow)
        val Sport : TextView = v.findViewById(R.id.sportreservation)
        val PlayerCourt : TextView = v.findViewById(R.id.playercourtreservation)
        val timeStart : TextView = v.findViewById(R.id.timereservationstart)
        val timeEnd : TextView = v.findViewById(R.id.timereservationend)
        val buttonDel : Button = v.findViewById(R.id.buttondelete)

       fun bind(srs: ShowReservationModel, holder: MyViewHolder, date: Date?,vm: ShowReservationsViewModel){
           val df = SimpleDateFormat("dd-MM-yyyy")
           Sport.text = srs.Sport
           PlayerCourt.text = srs.PlayerCourt
           timeStart.text = srs.StartHour.toString()
           timeEnd.text = srs.FinishHour.toString()

           val message = "Do you really want to delete the reservation?"
           buttonDel.setOnClickListener {
               showCustomDialogBox(holder.CardView.context, message, srs.Sport, srs.PlayerCourt,  df.format(srs.date).toString(),srs.time, srs.StartHour.toString(), srs.FinishHour.toString(),srs.id,vm )
           }
       }
        private fun showCustomDialogBox(context: Context, message: String?, sport: String, playercourt: String , date: String, time:String, start: String, end: String,id:Int,vm:ShowReservationsViewModel) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.reservation_delete_popup)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val tvMessage :TextView = dialog.findViewById(R.id.tvMessageDelete)
            val btnYes : Button = dialog.findViewById(R.id.btnYesDelete)
            val btnNo : Button = dialog.findViewById(R.id.btnNoDelete)
            val _sport : TextView = dialog.findViewById(R.id.sportSelectedDelete)
            val _playerCourt : TextView = dialog.findViewById(R.id.playerCourtSelectedDelete)
            val _date : TextView = dialog.findViewById(R.id.dateSelectedDelete)
            val _start :TextView = dialog.findViewById(R.id.startdelete)
            val _end :TextView = dialog.findViewById(R.id.enddelete)
            val x= java.text.SimpleDateFormat("dd-MM-yyyy");

            tvMessage.text = message
            _sport.text = sport
            _playerCourt.text = playercourt
            _date.text = date
            _start.text = start
            _end.text = end
            btnYes.setOnClickListener {
                vm.deleteReservation(id, x.parse(date),time,sport,start.toInt(),end.toInt(),playercourt )
                Toast.makeText(context, "Reservation deleted", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            btnNo.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        }
    }
