package hr.foi.air.mbankingapp.data.models

import com.google.gson.annotations.SerializedName

data class Racun(
    @SerializedName("iban") val iban: String,
    @SerializedName("vrijedi_od") val vrijediOd: String,
    @SerializedName("aktivnost") val aktivnost: String,
    @SerializedName("stanje") var stanje: Float = 0f,
    @SerializedName("vlasnik") val vlasnik: String,
    @SerializedName("valuta") val valuta: String,
    @SerializedName("vrsta_rac") val vrstaRacuna: String
)
