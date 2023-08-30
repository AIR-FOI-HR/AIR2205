package hr.foi.air.mbanking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import hr.foi.air.mbanking.api.AccountRequest
import hr.foi.air.mbanking.api.TransactionRequest
import hr.foi.air.mbanking.api.UserRequest
import hr.foi.air.mbanking.databinding.LayoutUserAccountBinding
import hr.foi.air.mbanking.entities.Account
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.transactionRecyclerView.TransactionAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONTokener
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: LayoutUserAccountBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private val client = OkHttpClient()
    private lateinit var glavniRacun: String
    private val userRequest = UserRequest()
    private val accountRequest = AccountRequest()
    private val transactionRequest = TransactionRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutUserAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        initializeLayout()
        onMenuPressed()
    }

    private fun getData(url: String): JSONArray{
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val podaci = JSONTokener(response.body!!.string()).nextValue() as JSONArray
            return podaci
        }
    }

    fun initializeLayout(){

        val id = getActiveUser()
        val trenutniKorisnik = id?.let { userRequest.getUser(it) }
        binding.username.text = "Dobrodošli ".plus(trenutniKorisnik?.ime)

        var racunIBAN = id?.let { getMainUserAccount(it) }


        if(binding.accountDetails.text == ""){
            binding.accountDetails.text = "Nema aktivnog računa"
        }


        if (racunIBAN != null) {
            getUserTransactions(racunIBAN)
        }
    }

    fun getMainUserAccount(id: Int): String?{
        val racuni = accountRequest.getAllAccounts()
        for(racun in racuni){
            if(racun.korisnik_id == id){

                val idVrsteRacuna = racun.vrsta_racuna_id
                val vrstaRacuna = getData("http://3.72.75.217/mBankingAPI/api/account_type/get.php?id=".plus(idVrsteRacuna))

                binding.accountDetails.text = vrstaRacuna.getJSONObject(0).getString("naziv")
                    .plus("\n")
                    .plus(racun.iban)
                    .plus("\n")
                    .plus("Raspoloživo: ")
                    .plus(racun.stanje)

                val gson = Gson()
                glavniRacun = gson.toJson(racun)
                return racun.iban
            }
        }
        return ""
    }

    fun getUserTransactions(racunIBAN: String){
        var listaTransakcija = mutableListOf<Transaction>()
        val transakcije = transactionRequest.getAllTransactions()
        for(transakcija in transakcije){
            if(racunIBAN == transakcija.platitelj_iban){
                listaTransakcija.add(transakcija)
            }
        }

        transactionAdapter = TransactionAdapter(listaTransakcija)
        binding.transactionsView.adapter = transactionAdapter
        binding.transactionsView.layoutManager = LinearLayoutManager(this)
    }

    fun onMenuPressed(){
        binding.menuButton.setOnClickListener{
            val intent1 = Intent(this, MenuActivity::class.java)
            intent1.putExtra("GlavniRacun", glavniRacun)
            startActivity(intent1)
        }
    }

    private fun getActiveUser(): Int?{
        val sharedPreferences = getSharedPreferences("ACTIVE_USER", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("USER_ID", 0)
    }
}