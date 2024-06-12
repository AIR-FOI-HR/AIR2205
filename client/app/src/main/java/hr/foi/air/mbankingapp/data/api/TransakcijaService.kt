package hr.foi.air.mbankingapp.data.api

import hr.foi.air.mbankingapp.data.models.Transakcija
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TransakcijaService {
    @GET("transakcije/racun/{iban}")
    suspend fun getTransakcijeRacuna(@Path("iban") iban: String): List<Transakcija>

    @GET("transakcije/korisnik/{id}")
    suspend fun getTransakcijeKorisnika(
        @Path("id") id: Int,
        @Query("numRows") numRows: Int = 0,
        @Query("vrsta_tran") vrstaTran: String = "",
        @Query("od_datuma") odDatuma: String = "",
        @Query("do_datuma") doDatuma: String = "",
        @Query("od_iznosa") odIznosa: String = "",
        @Query("do_iznosa") doIznosa: String = "",
    ): List<Transakcija>

    @GET("transakcije/{id}")
    suspend fun getTransakcija(@Path("id") id: Int): Transakcija

    @Headers("Content-Type: application/json")
    @POST("transakcije")
    suspend fun createTransakcija(@Body transakcija: Transakcija): Transakcija
}