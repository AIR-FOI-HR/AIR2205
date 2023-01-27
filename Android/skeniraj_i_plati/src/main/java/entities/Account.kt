package entities

data class Account(
    var iban: String,
    var stanje: Double,
    var aktivnost: Char,
    var korisnik_id: Int,
    var vrsta_racuna_id: Int,
    var qr_kod: String
)
