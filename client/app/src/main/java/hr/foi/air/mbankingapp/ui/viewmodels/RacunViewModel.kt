package hr.foi.air.mbankingapp.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.mbankingapp.data.models.Racun
import hr.foi.air.mbankingapp.data.repository.RacunRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class RacunViewModel: ViewModel() {
    private val repository = RacunRepository();
    var racun: MutableState<Racun?> = mutableStateOf(null)

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
}