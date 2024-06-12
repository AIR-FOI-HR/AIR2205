package hr.foi.air.mbankingapp.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.mbankingapp.data.context.Auth
import hr.foi.air.mbankingapp.data.models.Korisnik
import hr.foi.air.mbankingapp.data.repository.KorisnikRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class PostavkeViewModel: ViewModel() {
    private val repository = KorisnikRepository()
    var error = mutableStateOf(false);
    var errorText = mutableStateOf("");

    var success = mutableStateOf(false);

    fun setNoviPin(postojeciPin: String, noviPin: String, potvrdaPina: String) {
        if (postojeciPin.isEmpty() || noviPin.isEmpty() || potvrdaPina.isEmpty()) {
            error.value = true;
            errorText.value = "Nisu popunjena sva polja!";
            return;
        }

        if (!postojeciPin.equals(Auth.loggedUser!!.pin)) {
            error.value = true;
            errorText.value = "Pogrešan postojeći PIN!";
            return;
        }

        if (!noviPin.equals(potvrdaPina)) {
            error.value = true;
            errorText.value = "Novi PIN i potvrda PIN-a nisu jednaki!";
            return;
        }

        updateKorisnik(pin = noviPin);
    }

    fun updateKorisnik(
        pin: String? = null,
        telBroj: String? = null,
        email: String? = null
    ) {
        if (Auth.loggedUser == null) return;
        viewModelScope.launch {
            try {
                val korisnik = repository.updateUser(
                    Auth.loggedUser!!.id!!,
                    Korisnik(
                        pin =  pin ?: Auth.loggedUser!!.pin,
                        telBroj = telBroj ?: Auth.loggedUser!!.telBroj,
                        email = email ?: Auth.loggedUser!!.email
                    )
                )
                Auth.loggedUser = korisnik;
                success.value = true;
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