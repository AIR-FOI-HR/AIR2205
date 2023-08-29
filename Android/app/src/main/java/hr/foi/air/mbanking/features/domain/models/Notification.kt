package hr.foi.air.mbanking.features.domain.models

data class Notification(
    val obavijest_id: Int,
    val sadrzaj: String,
    val datum: String,
    val korisnik_id: String
)
