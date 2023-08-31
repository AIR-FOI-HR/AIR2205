package entities

data class Transaction(
    var transakcija_id: Int = -1,
    val platitelj_iban: String = "",
    var primatelj_iban: String = "",
    var iznos: Double = 0.0,
    var opis_placanja: String = "",
    var model: String = "",
    var poziv_na_broj: String = "",
    var datum_izvrsenja: String = "",
    var valuta_id: Int = 1
)