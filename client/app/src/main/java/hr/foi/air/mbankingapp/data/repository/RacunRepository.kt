package hr.foi.air.mbankingapp.data.repository

import hr.foi.air.mbankingapp.data.api.RetrofitInstance
import hr.foi.air.mbankingapp.data.models.Racun
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RacunRepository {
    private val racunService = RetrofitInstance.getRacunService;

    suspend fun getRacuniKorisnika(korId: Int): List<Racun> {
        return racunService.getRacuniKorinika(korId);
    }

    suspend fun getRacun(iban: String): Racun {
        return racunService.getRacun(iban);
    }

    suspend fun getRacunFromTelBroj(telBroj: String): Racun {
        return racunService.getRacunFromTelBroj(telBroj);
    }
}