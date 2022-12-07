package hr.foi.air.mbanking.entities

open class Account (
    var id: Int? = null,
    var username: String = "",
    var ime: String = "",
    var prezime: String = "",
    var vrstaRacuna: String = "",
    var iban: String = "",
    var promet: Double = 0.00,
    var stanje: Double = 0.00,
    var transakcije: MutableList<Transaction>
)