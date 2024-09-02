package hr.foi.air.mbankingapp.data.repository.placanja

import hr.foi.air.mbankingapp.data.models.Transakcija
import org.json.JSONException
import org.json.JSONObject

class QrPlacanje : PlacanjeRepository {
    override fun popuniPodatke(value: String): Transakcija? {
        try {
            var json = JSONObject(value);
            var primateljIban = json.getString("primatelj");
            var iznos = json.getString("iznos");
            var opisPlacanja = json.getString("opis");
            var model = json.getString("model");
            var pozivNaBroj = json.getString("pozivNaBroj");

            var transakcija = Transakcija(null, opisPlacanja, iznos, model, pozivNaBroj, null, null, null, primateljIban, null)
            return transakcija;
        } catch (e: JSONException) {
            return null;
        }
    }
}