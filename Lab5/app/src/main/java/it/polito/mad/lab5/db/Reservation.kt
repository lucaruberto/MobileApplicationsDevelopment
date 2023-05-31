package it.polito.mad.lab5.db

import java.util.Date

class Reservation(
    val discipline: String,
    val playgroundName : String,
    val date: Date,
    val oraInizio: Int,
    val oraFine : Int,
    val customRequest: String
){
    constructor() : this("", "", Date(), -1, -1, "")

    override fun toString(): String {
        return "$discipline $playgroundName $date $oraInizio-$oraFine $customRequest"
    }
}
