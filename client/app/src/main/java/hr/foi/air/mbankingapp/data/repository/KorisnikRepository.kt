package hr.foi.air.mbankingapp.data.repository

import hr.foi.air.mbankingapp.data.api.RetrofitInstance
import hr.foi.air.mbankingapp.data.models.Korisnik

class KorisnikRepository {
    private val korisnikService = RetrofitInstance.getKorisnikService;

    suspend fun getUsers(): List<Korisnik> {
        return korisnikService.getUsers();
    }

    suspend fun createUser(korisnik: Korisnik) : Korisnik {
        return korisnikService.createUser(korisnik);
    }
}