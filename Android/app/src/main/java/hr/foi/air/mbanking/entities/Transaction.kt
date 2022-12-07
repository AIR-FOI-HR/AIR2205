package hr.foi.air.mbanking.entities

open class Transaction (
    var opis: String = "",
    var iznos: Double = 0.00,
    var valuta: String = ""
)