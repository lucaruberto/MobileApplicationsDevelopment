package it.polito.mad.lab5.db

import java.time.LocalDate

data class Rating(
    var id: String? = "",
    var field: String? = "",
    var reviewText: String? = "",
    var score: Int? = 0,
    var user: ProvaUser? = ProvaUser(),
    var date: String? = ""
)