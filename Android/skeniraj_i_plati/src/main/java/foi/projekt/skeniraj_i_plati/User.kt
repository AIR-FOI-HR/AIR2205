package foi.projekt.skeniraj_i_plati

data class User(
    val korisnik_id: Int = 1,
    val ime: String,
    val prezime: String,
    val email: String,
    var adresa: String,
    var mobitel: String,
    var pin: String,
    var kod_za_oporavak: String
)
