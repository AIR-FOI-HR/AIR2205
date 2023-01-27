package hr.foi.air.mbanking.entities

data class User(
    val korisnik_id: Int = 1,
    val ime: String,
    val prezime: String,
    val email: String,
    val adresa: String,
    val mobitel: String,
    val pin: String,
    val kod_za_oporavak: String
)
