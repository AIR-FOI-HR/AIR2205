package hr.foi.air.mbankingapp.data.repository

import hr.foi.air.mbankingapp.data.api.RetrofitInstance
import hr.foi.air.mbankingapp.data.models.Obavijest
import hr.foi.air.mbankingapp.data.models.ObavijestProcitaj

class ObavijestRepository {
    private val repository = RetrofitInstance.getObavijestService;

    suspend fun getObavijestiKorisnika(korisnikId: Int): List<Obavijest> {
        return repository.getObavijestiKorisnika(korisnikId);
    }

    suspend fun getObavijest(obavijestId: Int): Obavijest {
        return repository.getObavijest(obavijestId);
    }

    suspend fun createObavijest(obavijest: Obavijest): Obavijest {
        return repository.createObavijest(obavijest);
    }

    suspend fun procitajObavijest(korisnikId: Int, obavijestId: Int) {
        repository.procitajObavijest(ObavijestProcitaj(korisnikId, obavijestId))
    }
}