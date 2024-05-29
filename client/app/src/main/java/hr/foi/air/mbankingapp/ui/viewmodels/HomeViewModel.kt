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
import hr.foi.air.mbankingapp.data.repository.RacunRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class HomeViewModel: ViewModel() {
    private val repository = RacunRepository();
    private val _racuni: MutableLiveData<List<Racun>> = MutableLiveData(mutableListOf());
    val racuni: LiveData<List<Racun>> = _racuni

    var error = mutableStateOf(false);
    var errorText = mutableStateOf("");

    init {
        loadRacuni()
    }

    private fun loadRacuni() {
        if (Auth.loggedUser == null) return;
        error.value = false;
        viewModelScope.launch {
            try {
                _racuni.value = repository.getRacuniKorisnika(Auth.loggedUser?.id!!);
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                Log.d("ErrorAPI", "racuni exception: ${response}");
                error.value = true;
                if (response != null) {
                    errorText.value = response;
                }
            }
        }
    }
}