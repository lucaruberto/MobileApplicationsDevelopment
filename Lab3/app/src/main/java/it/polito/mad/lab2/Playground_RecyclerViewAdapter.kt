package it.polito.mad.lab2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class Playground_RecyclerViewAdapter(val data : List<ReservationModel>) : RecyclerView.Adapter <Playground_RecyclerViewAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Playground_RecyclerViewAdapter.MyViewHolder {

    val v= LayoutInflater.from(parent.context).inflate(R.layout.cardview_reservation,parent,false)

        return MyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: Playground_RecyclerViewAdapter.MyViewHolder,
        position: Int) {
        val rs= data[position];
        holder.bind(rs, holder);
    }

    override fun getItemCount(): Int {
        return data.size
        }

    class  MyViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val StarHour: TextView = v.findViewById(R.id.Orainizio)
        val FinishHour: TextView = v.findViewById(R.id.Orafine)
        val CardView : CardView = v.findViewById(R.id.cardview)
        fun bind(rs: ReservationModel, holder: MyViewHolder){
            StarHour.text=rs.StartHour;
            FinishHour.text=rs.FinishHour;
            if(rs.StartHour=="8") {
                holder.CardView.setCardBackgroundColor(
                    ContextCompat.getColor(holder.CardView.context, R.color.purple_200)
                )
            }
        }
    }
}