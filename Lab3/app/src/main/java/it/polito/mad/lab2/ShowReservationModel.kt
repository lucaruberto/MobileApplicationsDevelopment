package it.polito.mad.lab2

import java.util.Date


class ShowReservationModel(Sport: String, PlayerCourt: String, date: Date?, StartHour: Int, FinishHour: Int){

    var Sport = Sport
        get() = field
    var PlayerCourt = PlayerCourt
        get() = field
    var date = date
        get() = field
    var StartHour = StartHour
        get() = field

    var FinishHour = FinishHour
        get() = field

}