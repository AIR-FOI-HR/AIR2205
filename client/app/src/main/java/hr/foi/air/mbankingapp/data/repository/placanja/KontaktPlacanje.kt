package hr.foi.air.mbankingapp.data.repository.placanja

import hr.foi.air.mbankingapp.data.models.Racun
import hr.foi.air.mbankingapp.data.models.Transakcija
import hr.foi.air.mbankingapp.data.repository.RacunRepository
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class KontaktPlacanje : PlacanjeRepository {
    override fun popuniPodatke(value: String): Transakcija? {
        return Transakcija(null,
            "Plaćanje prema kontaktu.",
            null,
            null,
            null,
            null,
            null,
            null,
            value,
            null
        )
    }

}