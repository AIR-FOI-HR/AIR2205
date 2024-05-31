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

    fun loadTransakcije() {
        if (Auth.loggedUser == null) return;
        errorTran.value = false;
        viewModelScope.launch {
            try {
                _transakcije.value = repository.getTransakcijeKorisnika(Auth.loggedUser?.id!!);
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