package hr.foi.air.mbankingapp.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.kontakti.ContactsProvider
import hr.foi.air.kontakti.models.Contact
import hr.foi.air.mbankingapp.data.models.Racun
import hr.foi.air.mbankingapp.data.repository.RacunRepository
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class KontaktiViewModel: ViewModel() {
    private val contactsProvider = ContactsProvider();
    private val racunRepository = RacunRepository();

    private val _kontakti: MutableLiveData<List<Contact>> = MutableLiveData(mutableListOf());
    val kontakti: LiveData<List<Contact>> = _kontakti;

    var racun = mutableStateOf<Racun?>(null);
    var error = mutableStateOf(false);
    var errorText = mutableStateOf("");

    var errorRacun = mutableStateOf(false);
    var errorRacunText = mutableStateOf("");

    fun loadContacts(context: Context) {
        try {
            _kontakti.value = contactsProvider.getContacts(context.contentResolver);
            if (_kontakti.value.isNullOrEmpty() == true) {
                error.value = true;
                errorText.value = "Nisu pronađeni kontakti na uređaju.";
            }
        } catch (ex: Exception) {
            error.value = true;
            errorText.value = ex.toString();
        }
    }

    fun fetchRacun(telBroj: String) {
        viewModelScope.launch {
            try {
                racun.value = racunRepository.getRacunFromTelBroj(telBroj);
            } catch (ex: HttpException) {
                val response = ex.response()?.errorBody()?.string()?.let {
                    JSONObject(JSONArray(JSONObject(it).getString("exception")).getString(0)).getString(("message"))
                };

                errorRacun.value = true;
                if (response != null) {
                    errorRacunText.value = response;
                }
            }
        }
    }
}