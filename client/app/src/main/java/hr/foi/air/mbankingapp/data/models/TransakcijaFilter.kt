package hr.foi.air.mbankingapp.data.models

import com.google.gson.annotations.SerializedName

data class TransakcijaFilter(
    @SerializedName("vrsta_tran") val vrstaTran: String? = null,
    @SerializedName("od_datuma") val odDatuma: String? = null,
    @SerializedName("do_datuma") val doDatuma: String? = null,
    @SerializedName("od_iznosa") val odIznosa: String? = null,
    @SerializedName("do_iznosa") val doIznosa: String? = null
)
