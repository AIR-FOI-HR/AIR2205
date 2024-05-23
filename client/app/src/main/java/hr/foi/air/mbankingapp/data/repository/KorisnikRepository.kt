package hr.foi.air.mbankingapp.data.repository

import hr.foi.air.mbankingapp.data.api.RetrofitInstance
import hr.foi.air.mbankingapp.data.models.Korisnik

class KorisnikRepository {
    private val korisnikService = RetrofitInstance.getKorisnikService;

    suspend fun createUser(korisnik: Korisnik) : Korisnik {
        return korisnikService.createUser(korisnik);
    }

    suspend fun updateUser(id: Int, korisnik: Korisnik) : Korisnik {
        return korisnikService.updateUser(id, korisnik);
    }

    suspend fun authUser(korisnik: Korisnik) : Korisnik {
        return korisnikService.authUser(korisnik);
    }

    suspend fun restoreUser(korisnik: Korisnik) : Korisnik {
        return korisnikService.restoreUser(korisnik);
    }
}