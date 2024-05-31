package hr.foi.air.mbankingapp.data.api

import hr.foi.air.mbankingapp.data.models.Transakcija
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransakcijaService {
    @GET("transakcije/{iban}")
    suspend fun getTransakcijeRacuna(@Path("iban") iban: String): List<Transakcija>

    @GET("transakcije/korisnik/{id}")
    suspend fun getTransakcijeKorisnika(@Path("id") id: Int, @Query("numRows") numRows: Int = 0): List<Transakcija>
}