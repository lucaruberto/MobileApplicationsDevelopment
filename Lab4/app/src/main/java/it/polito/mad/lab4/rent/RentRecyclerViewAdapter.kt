package it.polito.mad.lab3

/*
class PlaygroundRecyclerViewAdapter(val data : List<ShowReservationModel>, val date: Date?, private val dropmenu : String, private val dropmenufields: String,
                                    private val vm:SearchPlaygroundViewModel) : RecyclerView.Adapter <PlaygroundRecyclerViewAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): MyViewHolder {

        val v= LayoutInflater.from(parent.context).inf late(R.layout.cardview_reservation,parent,false)
        return MyViewHolder(v,vm)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int) {
        val rs= data[position];
        holder.bind(rs, holder, date!!,  dropmenu, dropmenufields,vm);
    }

    override fun getItemCount(): Int {
        return data.size
        }

    class  MyViewHolder(v: View, vm:SearchPlaygroundViewModel) : RecyclerView.ViewHolder(v){
        private val startHour: TextView = v.findViewById(R.id.Orainizio)
        private val finishHour: TextView = v.findViewById(R.id.Orafine)
        val cardView : CardView = v.findViewById(R.id.cardview)
        fun bind(rs: ShowReservationModel, holder: MyViewHolder, date: Date?, dropmenu: String, dropmenufields: String,vm: SearchPlaygroundViewModel) {
            startHour.text = rs.startHour.toString();
            finishHour.text = rs.finishHour.toString();

            cardView.setOnClickListener {
                val message = "Are you sure?"
                showCustomDialogBox(holder.cardView.context, message, date!!, dropmenu, dropmenufields, rs.startHour.toString(), rs.finishHour.toString(), vm)
            }
        }

        @SuppressLint("ResourceAsColor")
        private fun showCustomDialogBox(context: Context, message: String?, date: Date?, dropmenu: String, dropmenufields: String, start: String, end: String,vm:SearchPlaygroundViewModel) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.reservation_popup)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val df = SimpleDateFormat("dd-MM-yyyy", Locale.ITALY)
            val tvMessage :TextView = dialog.findViewById(R.id.tvMessage)
            val btnYes : Button = dialog.findViewById(R.id.btnYes)
            val btnNo : Button = dialog.findViewById(R.id.btnNo)
            val sport : TextView = dialog.findViewById(R.id.sportSelected)
            val playerCourt : TextView = dialog.findViewById(R.id.playerCourtSelected)
            val dateS : TextView = dialog.findViewById(R.id.dateSelected)
            val _start : TextView = dialog.findViewById(R.id.start)
            val _end : TextView = dialog.findViewById(R.id.end)
            val customRequest: EditText = dialog.findViewById(R.id.customrequest)
            tvMessage.text = message
            sport.text = dropmenu
            dateS.text = df.format(date).toString()
            _start.text = start
            _end.text = end
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
                            dropmenufields,
                            customRequest.text.toString()
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
*/
