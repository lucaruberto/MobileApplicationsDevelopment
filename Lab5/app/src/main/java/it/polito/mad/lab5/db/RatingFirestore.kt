package it.polito.mad.lab5.db

data class RatingFirestore(
    var id: String? = "",
    var field: String? = "",
    var reviewText: String? = "",
    var score: Int? = 0,
    var user: String? = ""
)