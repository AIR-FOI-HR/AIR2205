package hr.foi.air.mbankingapp.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import hr.foi.air.mbankingapp.data.models.Korisnik
import hr.foi.air.mbankingapp.data.repository.KorisnikRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException


class RegisterViewModel : ViewModel() {
    private val repository = KorisnikRepository();

    var ime = mutableStateOf("");
    var prezime = mutableStateOf("");
    var email = mutableStateOf("");
    var oib = mutableStateOf("");
    var pin = mutableStateOf("");

    fun checkUserData(context: Context, ime: String, prezime: String, email: String, oib: String) : Boolean {
        if (ime.isEmpty() || prezime.isEmpty() || email.isEmpty() || oib.isEmpty()) {
            Toast.makeText(context, "Nisu popunjeni svi podaci!", Toast.LENGTH_LONG).show();
            return false;
        }

        this.ime.value = ime;
        this.prezime.value = prezime;
        this.email.value = email;
        this.oib.value = oib;
        return true;
    }

    fun checkPin(context: Context, pin: String, pinPotvrda: String) : Boolean {
        if (pin.isEmpty() || pinPotvrda.isEmpty()) {
            Toast.makeText(context, "Nisu popunjeni svi podaci!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (pin.length < 4 || pin.length > 15) {
            Toast.makeText(context, "Dužina PIN-a mora biti između 4 i 15 znakova.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!pin.equals(pinPotvrda)) {
            Toast.makeText(context, "Potvrda PIN-a nije uspjela!", Toast.LENGTH_LONG).show();
            return false;
        }

        this.pin.value = pin;
        return true;
    }

    fun register(context: Context, navController: NavController) {
        viewModelScope.launch {
            try {
                val korisnik = repository.createUser(
                    Korisnik(
                        ime = ime.value,
                        prezime = prezime.value,
                        oib = oib.value,
                        email = email.value,
                        pin = pin.value,
                    )
                )

                Toast.makeText(context, "Registracija uspješna!", Toast.LENGTH_LONG).show();
                navController.navigate("login") {
                    popUpTo("register") {
                        inclusive = true
                    }
                }
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };
                Log.d("ErrorAPI", "register exception: ${response}")
                Toast.makeText(context, "Pogreška: ${response}", Toast.LENGTH_LONG).show();
            }
        }
    }
}

