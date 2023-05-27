package it.polito.mad.lab5.db

class RatingFirestore(id: String, field: String, reviewText: String, score: Int, user: String) {
    constructor() : this ("","","",0,"")
    val id = id
    val field = field
    val reviewText = reviewText
    val score = score
    val user = user
}