package hr.foi.air.mbankingapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://209.38.220.190/mbanking/api/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getKorisnikService: KorisnikService by lazy {
        retrofit.create(KorisnikService::class.java)
    }

    val getRacunService: RacunService by lazy {
        retrofit.create(RacunService::class.java)
    }

    val getTransakcijaService: TransakcijaService by lazy {
        retrofit.create(TransakcijaService::class.java)
    }
}