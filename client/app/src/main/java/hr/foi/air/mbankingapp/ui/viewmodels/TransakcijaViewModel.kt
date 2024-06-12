package hr.foi.air.mbankingapp.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.mbankingapp.data.context.Auth
import hr.foi.air.mbankingapp.data.models.Transakcija
import hr.foi.air.mbankingapp.data.repository.TransakcijaRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class TransakcijaViewModel: ViewModel() {
    private val repository = TransakcijaRepository();
    var transakcija: MutableState<Transakcija?> = mutableStateOf(null)

    private val _transakcije: MutableLiveData<List<Transakcija>> = MutableLiveData(mutableListOf());
    val transakcije: LiveData<List<Transakcija>> = _transakcije

    var errorTran = mutableStateOf(false);
    var errorTranText = mutableStateOf("");

    var filter = mutableStateOf("");
    var resetData = mutableStateOf(false);

    fun loadTransakcija(id: Int) {
        viewModelScope.launch {
            try {
                transakcija.value = repository.getTransakcija(id);
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };
                Log.d("ErrorAPI", "transakcija exception: ${response}");
            }
        }
    }

    fun loadTransakcije(
        vrstaTran: String = "",
        odDatuma: String = "",
        doDatuma: String = "",
        odIznosa: String = "",
        doIznosa: String = ""
    ) {
        if (Auth.loggedUser == null) return;
        errorTran.value = false;

        filter.value = ""

        if (vrstaTran != "") filter.value += "Vrsta transakcije: $vrstaTran; "
        if (odDatuma != "") filter.value += "Od datuma: $odDatuma; "
        if (doDatuma != "") filter.value += "Do datuma: $doDatuma; "
        if (odIznosa != "") filter.value += "Od iznosa: $odIznosa; "
        if (doIznosa != "") filter.value += "Do iznosa: $doIznosa; "

        viewModelScope.launch {
            try {
                _transakcije.value = repository.getTransakcijeKorisnika(
                    Auth.loggedUser?.id!!,
                    0,
                    vrstaTran,
                    odDatuma,
                    doDatuma,
                    odIznosa,
                    doIznosa
                );
            } catch (ex: HttpException) {
                _transakcije.value = emptyList()
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                Log.d("ErrorAPI", "transakcije exception: ${response}");
                errorTran.value = true;
                if (response != null) {
                    errorTranText.value = response;
                }
            }
        }
    }

    fun createTransakcija(racunIban: String, primateljIban: String, opisPlacanja: String, iznos: String, model: String, pozivNaBroj: String) {
        viewModelScope.launch {
            try {
                transakcija.value = repository.createTransakcija(
                    Transakcija(
                        opisPlacanja = opisPlacanja,
                        model =  model,
                        pozivNaBroj = pozivNaBroj,
                        iznos = iznos,
                        platiteljIban = racunIban,
                        primateljIban = primateljIban
                    )
                )
                resetData.value = true;
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                Log.d("ErrorAPI", "transakcija exception: ${response}");
                errorTran.value = true;
                if (response != null) {
                    errorTranText.value = response;
                }
            }
        }
    }
}