package hr.foi.air.mbankingapp.data.api

import hr.foi.air.mbankingapp.data.models.Korisnik
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface KorisnikService {
    @GET("users")
    suspend fun getUsers(): List<Korisnik>

    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun createUser(@Body korisnik: Korisnik): Korisnik
}