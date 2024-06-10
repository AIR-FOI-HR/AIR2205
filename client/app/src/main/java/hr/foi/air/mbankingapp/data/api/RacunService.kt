package hr.foi.air.mbankingapp.data.api

import hr.foi.air.mbankingapp.data.models.Racun
import retrofit2.http.GET
import retrofit2.http.Path

interface RacunService {
    @GET("racuni/korisnik/{kor_id}")
    suspend fun getRacuniKorinika(@Path("kor_id") korId: Int) : List<Racun>

    @GET("racuni/{iban}")
    suspend fun getRacun(@Path("iban") iban: String) : Racun

    @GET("racuni/telbroj/{tel_broj}")
    suspend fun getRacunFromTelBroj(@Path("tel_broj") telBroj: String) : Racun
}