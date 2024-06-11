package hr.foi.air.mbankingapp.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.mbankingapp.data.context.Auth
import hr.foi.air.mbankingapp.data.models.Obavijest
import hr.foi.air.mbankingapp.data.repository.ObavijestRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class ObavijestiViewModel: ViewModel() {
    private val repository = ObavijestRepository();
    private val _obavijesti: MutableLiveData<List<Obavijest>> = MutableLiveData(mutableListOf());
    val obavijesti: LiveData<List<Obavijest>> = _obavijesti

    var obavijest: MutableState<Obavijest?> = mutableStateOf(null);

    var error = mutableStateOf(false);
    var errorText = mutableStateOf("");

    fun fetchObavijesti() {
        if (Auth.loggedUser == null) return;
        viewModelScope.launch {
            try {
                _obavijesti.value = repository.getObavijestiKorisnika(Auth.loggedUser!!.id!!)
                if (_obavijesti.value!!.isEmpty()) {
                    error.value = true;
                    errorText.value = "Nisu pronađene obavijesti za korisnika.";
                }
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                error.value = true;
                if (response != null) {
                    errorText.value = response;
                }
            }
        }
    }

    fun fetchObavijest(obavijestId: Int) {
        viewModelScope.launch {
            try {
                obavijest.value = repository.getObavijest(obavijestId);
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                error.value = true;
                if (response != null) {
                    errorText.value = response;
                }
            }
        }
    }

    fun procitajObavijest(obavijestId: Int) {
        if (Auth.loggedUser == null) return;
        viewModelScope.launch {
            try {
                repository.procitajObavijest(Auth.loggedUser!!.id!!, obavijestId);
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                error.value = true;
                if (response != null) {
                    errorText.value = response;
                }
            }
        }
    }
}