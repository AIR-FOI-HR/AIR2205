package hr.foi.air.mbankingapp.data.repository.placanja

import hr.foi.air.mbankingapp.data.models.Transakcija

interface PlacanjeRepository {
    fun popuniPodatke(value: String): Transakcija?
}