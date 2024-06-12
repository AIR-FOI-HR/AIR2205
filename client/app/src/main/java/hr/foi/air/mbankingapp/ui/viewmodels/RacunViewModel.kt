package hr.foi.air.mbankingapp.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.mbankingapp.data.context.Auth
import hr.foi.air.mbankingapp.data.models.Racun
import hr.foi.air.mbankingapp.data.repository.RacunRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class RacunViewModel: ViewModel() {
    private val repository = RacunRepository();
    var racun: MutableState<Racun?> = mutableStateOf(null)

    private val _racuni: MutableLiveData<List<Racun>> = MutableLiveData(mutableListOf());
    val racuni: LiveData<List<Racun>> = _racuni

    var errorRacuni = mutableStateOf(false);
    var errorRacuniText = mutableStateOf("");

    fun loadRacun(iban: String) {
        viewModelScope.launch {
            try {
                racun.value = repository.getRacun(iban);
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };
                Log.d("ErrorAPI", "racuni exception: ${response}");
            }
        }
    }

    fun loadRacuni() {
        if (Auth.loggedUser == null) return;
        errorRacuni.value = false;
        viewModelScope.launch {
            try {
                _racuni.value = repository.getRacuniKorisnika(Auth.loggedUser?.id!!);
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
}