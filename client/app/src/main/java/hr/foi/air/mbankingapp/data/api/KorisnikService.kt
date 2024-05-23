package hr.foi.air.mbankingapp.data.api

import hr.foi.air.mbankingapp.data.models.Korisnik
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface KorisnikService {
    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun createUser(@Body korisnik: Korisnik): Korisnik

    @Headers("Content-Type: application/json")
    @PATCH("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body korisnik: Korisnik): Korisnik

    @Headers("Content-Type: application/json")
    @POST("users/auth")
    suspend fun authUser(@Body korisnik: Korisnik): Korisnik

    @Headers("Content-Type: application/json")
    @POST("users/restore")
    suspend fun restoreUser(@Body korisnik: Korisnik): Korisnik
}