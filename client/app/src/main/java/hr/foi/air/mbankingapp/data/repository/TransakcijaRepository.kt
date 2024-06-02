package hr.foi.air.mbankingapp.data.repository

import hr.foi.air.mbankingapp.data.api.RetrofitInstance
import hr.foi.air.mbankingapp.data.models.Transakcija
import hr.foi.air.mbankingapp.data.models.TransakcijaFilter
import retrofit2.http.Query

class TransakcijaRepository {
    private val transakcijaService = RetrofitInstance.getTransakcijaService;

    suspend fun getTransakcijeKorisnika(
        id: Int,
        numRows: Int = 0,
        vrstaTran: String = "",
        odDatuma: String = "",
        doDatuma: String = "",
        odIznosa: String = "",
        doIznosa: String = "",
    ) : List<Transakcija> {
        return transakcijaService.getTransakcijeKorisnika(id, numRows, vrstaTran, odDatuma, doDatuma, odIznosa, doIznosa);
    }

    suspend fun getTransakcijeRacuna(iban: String) : List<Transakcija> {
        return transakcijaService.getTransakcijeRacuna(iban);
    }

    suspend fun getTransakcija(id: Int) : Transakcija {
        return transakcijaService.getTransakcija(id);
    }
}