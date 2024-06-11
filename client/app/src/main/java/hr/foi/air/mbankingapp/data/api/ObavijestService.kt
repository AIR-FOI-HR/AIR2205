package hr.foi.air.mbankingapp.data.api

import hr.foi.air.mbankingapp.data.models.Obavijest
import hr.foi.air.mbankingapp.data.models.ObavijestProcitaj
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ObavijestService {
    @GET("obavijesti/korisnik/{kor_id}")
    suspend fun getObavijestiKorisnika(@Path("kor_id") korId: Int) : List<Obavijest>

    @GET("obavijesti/{obav_id}")
    suspend fun getObavijest(@Path("obav_id") obavijestId: Int) : Obavijest

    @Headers("Content-Type: application/json")
    @POST("obavijesti")
    suspend fun createObavijest(@Body obavijest: Obavijest): Obavijest

    @Headers("Content-Type: application/json")
    @PATCH("obavijesti/procitaj")
    suspend fun procitajObavijest(@Body obavijest: ObavijestProcitaj)
}