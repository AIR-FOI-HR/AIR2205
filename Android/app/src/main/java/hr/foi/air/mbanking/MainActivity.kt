package hr.foi.air.mbanking

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hr.foi.air.mbanking.LogInActivity.Companion.currentUser
import hr.foi.air.mbanking.databinding.LayoutUserAccountBinding
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.transactionRecyclerView.TransactionAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: LayoutUserAccountBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private val client = OkHttpClient()
    private lateinit var glavniRacun: JSONObject

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

        val id = 2
        val trenutniKorisnik = getData("http://20.67.25.104/mBankingAPI/api/user/get.php?id=".plus(id))
        binding.username.text = "Dobrodošli ".plus(trenutniKorisnik.getJSONObject(0).getString("ime"))

        var racunIBAN = ""
        val racuni = getData("http://20.67.25.104/mBankingAPI/api/account/get_all.php")
        for(i in 0 until racuni.length()){
            val racun = racuni.getJSONObject(i)
            if(racun.getInt("korisnik_id") == id){
                racunIBAN = racun.getString("iban")
                glavniRacun = racun
                val idVrsteRacuna = racun.getInt("vrsta_racuna_id")
                val vrstaRacuna = getData("http://20.67.25.104/mBankingAPI/api/account_type/get.php?id=".plus(idVrsteRacuna))

                binding.accountDetails.text = vrstaRacuna.getJSONObject(0).getString("naziv")
                    .plus("\n")
                    .plus(racunIBAN)
                    .plus("\n")
                    .plus("Raspoloživo: ")
                    .plus(racun.getString("stanje"))


            }
        }
        if(binding.accountDetails.text == ""){
            binding.accountDetails.text = "Nema aktivnog računa"
        }

        var listaTransakcija = mutableListOf<Transaction>()
        val sveTransakcije = getData("http://20.67.25.104/mBankingAPI/api/transaction/get_all.php")
        for(i in 0 until sveTransakcije.length()){
            val transakcija = sveTransakcije.getJSONObject(i)

            if(racunIBAN == transakcija.getString("iban")){
                val vrsta = getData("http://20.67.25.104/mBankingAPI/api/transaction_type/get.php?id=".plus(transakcija.getInt("vrsta_transakcije_id")))
                val valuta = getData("http://20.67.25.104/mBankingAPI/api/currency/get.php?id=".plus(transakcija.getInt("valuta_id")))
                // I DONT KNOW WHAT THE FUCK THIS DOES
                //val sadrzaj = Transaction(vrsta.getJSONObject(0).getString("naziv"), transakcija.getDouble("iznos") , valuta.getJSONObject(0).getString("oznaka"), " ")

                //listaTransakcija.add(transakcija)
            }
        }

        transactionAdapter = TransactionAdapter(listaTransakcija)
        binding.transactionsView.adapter = transactionAdapter
        binding.transactionsView.layoutManager = LinearLayoutManager(this)

    }

    fun onMenuPressed(){
        binding.menuButton.setOnClickListener{
            val intent1 = Intent(this, MenuActivity::class.java)
            intent1.putExtra("GlavniRacun", glavniRacun.toString())
            startActivity(intent1)
        }
    }
}