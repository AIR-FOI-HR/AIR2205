package hr.foi.air.mbankingapp.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import hr.foi.air.mbankingapp.data.context.Auth
import hr.foi.air.mbankingapp.data.models.Korisnik
import hr.foi.air.mbankingapp.data.repository.KorisnikRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel : ViewModel() {
    private val repository = KorisnikRepository();

    private var restoreKorisnik = mutableStateOf(Korisnik());

    fun login(context: Context, email: String, pin: String, onSuccesfullLogin: () -> Unit) {
        if (email.isEmpty() || pin.isEmpty()) {
            Toast.makeText(context, "Nisu popunjeni svi podaci!", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModelScope.launch {
            try {
                val korisnik = repository.authUser(Korisnik(email = email, pin = pin));
                Auth.loggedUser = korisnik;
                Toast.makeText(context, "Prijava uspješna!", Toast.LENGTH_SHORT).show();
                onSuccesfullLogin();
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                Log.d("ErrorAPI", "login exception: ${response}");
                Toast.makeText(context, "Pogreška: ${response}", Toast.LENGTH_SHORT).show();
            }
        }
    }

    fun restore(context: Context, email: String, onNavigateToNext: () -> Unit) {
        if (email.isEmpty()) {
            Toast.makeText(context, "E-mail nije upisan!", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModelScope.launch {
            try {
                val korisnik = repository.restoreUser(Korisnik(email = email));
                restoreKorisnik.value = korisnik;
                Toast.makeText(context, "Mail je poslan!", Toast.LENGTH_SHORT).show();
                onNavigateToNext();
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                Log.d("ErrorAPI", "restore exception: ${response}");
                Toast.makeText(context, "Pogreška: ${response}", Toast.LENGTH_SHORT).show();
            }
        }
    }

    fun updatePin(context: Context, kod: String, noviPin: String, noviPinPotvr: String, onNavigateToLogin: () -> Unit) {
        if (restoreKorisnik.value.id == null) return;
        if (kod.isEmpty() || noviPin.isEmpty() || noviPinPotvr.isEmpty()) {
            Toast.makeText(context, "Nisu popunjeni svi podaci!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!restoreKorisnik.value.kod.equals(kod)) {
            Toast.makeText(context, "Kod za oporavak je pogrešan!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!noviPin.equals(noviPinPotvr)) {
            Toast.makeText(context, "Potvrda novog PIN-a je pogrešna!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noviPin.length < 4 || noviPin.length > 15) {
            Toast.makeText(context, "Dužina novog PIN-a mora biti između 4 i 15 znakova.", Toast.LENGTH_SHORT).show();
            return;
        }

        restoreKorisnik.value.pin = noviPin
        restoreKorisnik.value.kod = ""
        viewModelScope.launch {
            try {
                val korisnik = repository.updateUser(restoreKorisnik.value.id!!, restoreKorisnik.value);
                Toast.makeText(context, "Novi PIN je postavljen!", Toast.LENGTH_SHORT).show();
                onNavigateToLogin()
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                Log.d("ErrorAPI", "restore exception: ${response}");
                Toast.makeText(context, "Pogreška: ${response}", Toast.LENGTH_SHORT).show();
            }
        }
    }
}