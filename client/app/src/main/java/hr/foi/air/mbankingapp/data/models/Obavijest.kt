package hr.foi.air.mbankingapp.data.models

import com.google.gson.annotations.SerializedName

data class Obavijest(
    @SerializedName("obav_id") val id: Int? = null,
    @SerializedName("naslov") val naslov: String,
    @SerializedName("tekst") val tekst: String,
    @SerializedName("datum") val datum: String? = null,
    @SerializedName("procitano") val procitano: Boolean? = null
)
