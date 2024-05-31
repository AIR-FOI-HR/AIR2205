package hr.foi.air.mbankingapp.data.repository

import hr.foi.air.mbankingapp.data.api.RetrofitInstance
import hr.foi.air.mbankingapp.data.models.Transakcija

class TransakcijaRepository {
    private val transakcijaService = RetrofitInstance.getTransakcijaService;

    suspend fun getTransakcijeKorisnika(id: Int, numRows: Int = 0) : List<Transakcija> {
        return transakcijaService.getTransakcijeKorisnika(id, numRows);
    }

    suspend fun getTransakcijeRacuna(iban: String) : List<Transakcija> {
        return transakcijaService.getTransakcijeRacuna(iban);
    }
}