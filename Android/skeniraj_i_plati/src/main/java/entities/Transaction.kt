package entities

import java.util.Date

data class Transaction(
    var iznos: Double,
    var opis_placanja: String,
    var model: String,
    var poziv_na_broj: String,
    var datum_izvrsenja: String,
    var vrsta_transakcije_vrsta_transakcije_id: Int,
    var racun_iban: String,
    var valuta_id: Int
)
