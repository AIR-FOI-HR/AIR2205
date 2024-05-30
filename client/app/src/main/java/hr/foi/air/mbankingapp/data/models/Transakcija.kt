package hr.foi.air.mbankingapp.data.models

import com.google.gson.annotations.SerializedName

data class Transakcija(
    @SerializedName("tran_id") val id: Int? = null,
    @SerializedName("opis_placanja") val opisPlacanja: String,
    @SerializedName("iznos") val iznos: String,
    @SerializedName("model") val model: String,
    @SerializedName("datum") val datum: String,
    @SerializedName("platitelj_iban") val platiteljIban: String,
    @SerializedName("platitelj_vlasnik") val platiteljVlasnik: String? = "",
    @SerializedName("primatelj_iban") val primateljIban: String,
    @SerializedName("primatelj_vlasnik") val primateljVlasnik: String? = ""
)
