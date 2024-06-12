package hr.foi.air.mbankingapp.data.models

import com.google.gson.annotations.SerializedName

data class ObavijestProcitaj(
    @SerializedName("kor_id") val korisnikId: Int,
    @SerializedName("obav_id") val obavijestId: Int
)
