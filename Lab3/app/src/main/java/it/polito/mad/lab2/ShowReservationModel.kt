package it.polito.mad.lab2

import java.util.Date


class ShowReservationModel(id: Int,Sport: String, PlayerCourt: String, date: Date?,time:String, StartHour: Int, FinishHour: Int){

    var id = id
        get()=field

    var Sport = Sport
        get() = field
    var PlayerCourt = PlayerCourt
        get() = field
    var date = date
        get() = field
    var time = time
        get() = field
    var StartHour = StartHour
        get() = field

    var FinishHour = FinishHour
        get() = field

}