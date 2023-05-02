package it.polito.mad.lab3

import java.util.Date


class ShowReservationModel(var id: Int,
                           var sport: String,
                           var playerCourt: String,
                           var date: Date?,
                           var time: String, var startHour: Int, var finishHour: Int,
                           var customRequest: String
){}