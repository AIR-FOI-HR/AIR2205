package hr.foi.air.mbankingapp.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.mbankingapp.data.models.Korisnik
import hr.foi.air.mbankingapp.data.repository.KorisnikRepository
import kotlinx.coroutines.launch


class RegisterViewModel : ViewModel() {
    private val repository = KorisnikRepository();

    var ime = mutableStateOf("");
    var prezime = mutableStateOf("");
    var email = mutableStateOf("");
    var oib = mutableStateOf("");
    var pin = mutableStateOf("");
    var kod = mutableStateOf("");

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

    fun checkKod(context: Context, kod: String, kodPotvrda: String) : Boolean {
        if (kod.isEmpty() || kodPotvrda.isEmpty()) {
            Toast.makeText(context, "Nisu popunjeni svi podaci!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (kod.length < 5) {
            Toast.makeText(context, "Dužina koda za oporavak mora biti najmanje 5 znakova.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!kod.equals(kodPotvrda)) {
            Toast.makeText(context, "Potvrda koda za oporavak nije uspjela!", Toast.LENGTH_LONG).show();
            return false;
        }

        this.kod.value = kod;
        return true;
    }

    fun register(context: Context) {
        viewModelScope.launch {
            try {
                val korisnik = repository.createUser(
                    Korisnik(
                        ime = ime.value,
                        prezime = prezime.value,
                        oib = oib.value,
                        email = email.value,
                        pin = pin.value,
                        kod = kod.value
                    )
                )

                Toast.makeText(context, "Registracija uspješna!", Toast.LENGTH_LONG).show();
            } catch (ex: Exception) {
                Log.d("ErrorAPI", "register exception: ${ex.message}")
            }
        }
    }
}

