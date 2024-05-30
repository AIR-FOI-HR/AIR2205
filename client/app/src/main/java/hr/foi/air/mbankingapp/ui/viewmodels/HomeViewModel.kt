package hr.foi.air.mbankingapp.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.mbankingapp.data.context.Auth
import hr.foi.air.mbankingapp.data.models.Racun
import hr.foi.air.mbankingapp.data.models.Transakcija
import hr.foi.air.mbankingapp.data.repository.RacunRepository
import hr.foi.air.mbankingapp.data.repository.TransakcijaRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class HomeViewModel: ViewModel() {
    private val racunRepository = RacunRepository();
    private val transakcijaRepository = TransakcijaRepository()

    private val _racuni: MutableLiveData<List<Racun>> = MutableLiveData(mutableListOf());
    val racuni: LiveData<List<Racun>> = _racuni

    private val _transakcije: MutableLiveData<List<Transakcija>> = MutableLiveData(mutableListOf());
    val transakcije: LiveData<List<Transakcija>> = _transakcije

    var errorRacuni = mutableStateOf(false);
    var errorRacuniText = mutableStateOf("");

    var errorTran = mutableStateOf(false);
    var errorTranText = mutableStateOf("");

    init {
        loadRacuni()
        loadTransakcije()
    }

    private fun loadRacuni() {
        if (Auth.loggedUser == null) return;
        errorRacuni.value = false;
        viewModelScope.launch {
            try {
                _racuni.value = racunRepository.getRacuniKorisnika(Auth.loggedUser?.id!!);
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                Log.d("ErrorAPI", "racuni exception: ${response}");
                errorRacuni.value = true;
                if (response != null) {
                    errorRacuniText.value = response;
                }
            }
        }
    }

    private fun loadTransakcije() {
        if (Auth.loggedUser == null) return;
        errorTran.value = false;
        viewModelScope.launch {
            try {
                _transakcije.value = transakcijaRepository.getTransakcijeKorisnika(Auth.loggedUser?.id!!, 5);
            } catch (ex: HttpException) {
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
}