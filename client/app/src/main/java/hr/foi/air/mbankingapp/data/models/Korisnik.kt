package hr.foi.air.mbankingapp.data.models

import com.google.gson.annotations.SerializedName

data class Korisnik(
    @SerializedName("kor_id") val id: Int? = null,
    @SerializedName("ime") val ime: String?,
    @SerializedName("prezime") val prezime: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("oib") val oib: String?,
    @SerializedName("pin") val pin: String?,
    @SerializedName("kod") val kod: String?,
    @SerializedName("tel_broj") val telBroj: String? = ""
)
