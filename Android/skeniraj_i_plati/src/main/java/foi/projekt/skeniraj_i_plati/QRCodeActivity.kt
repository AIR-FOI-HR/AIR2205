package foi.projekt.skeniraj_i_plati

import android.content.res.TypedArray
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import entities.Account
import entities.Transaction
import foi.projekt.skeniraj_i_plati.databinding.LayoutQrcodeBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class QRCodeActivity: AppCompatActivity() {
    private lateinit var binding: LayoutQrcodeBinding
    private lateinit var glavniRacun: JSONObject
    private val client = OkHttpClient()
    private val JSON = "application/json; charset=utf-8".toMediaType()
    private lateinit var ibanQRkoda: String
    private lateinit var dataFromQRCode: Array<String>
    private lateinit var transaction1: Transaction
    private lateinit var transaction2: Transaction

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val codeData = intent.getStringExtra("QRCodeData").toString()
        glavniRacun = JSONObject(intent.getStringExtra("GlavniRacun").toString())

        dataFromQRCode = codeData.split("\\R".toRegex()).toTypedArray()
        ibanQRkoda = dataFromQRCode[6]


        binding = LayoutQrcodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.QRCodeDetails.text = codeData//dataFromQRCode[6]

        onBackArrowPressed()
        onOdustaniPressed()
        onPlatiPressed()
    }

    private fun getData(url: String): JSONArray {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val podaci = JSONTokener(response.body!!.string()).nextValue() as JSONArray
            return podaci
        }
    }

    private fun postData(url: String, jsonData: String): Boolean{
        val request = Request.Builder()
            .url(url)
            .post(jsonData.toRequestBody(JSON))
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful){
                throw IOException("Unexpected code $response")
                return false
            }
            return true
        }
    }

    private fun onBackArrowPressed(){
        binding.buttonBack.setOnClickListener(){
            finish()
        }
    }

    private fun onOdustaniPressed(){
        binding.buttonOdustani.setOnClickListener(){
            finish()
        }
    }

    private fun onPlatiPressed(){
        binding.buttonPlati.setOnClickListener(){
            val dohvacenRacun = getData("http://20.67.25.104/mBankingAPI/api/account/get.php?iban=".plus(ibanQRkoda))

            val gson = Gson()
            var racunPrimatelja = gson.fromJson(dohvacenRacun.getJSONObject(0).toString(), Account::class.java)
            var racunPlatitelja = gson.fromJson(glavniRacun.toString(), Account::class.java)
            val regex = """(\d+)""".toRegex()
            val amount = regex.find(dataFromQRCode[7])
            racunPrimatelja.stanje = dohvacenRacun.getJSONObject(0).getDouble("stanje") + amount!!.value.toDouble()
            racunPlatitelja.stanje = glavniRacun.getDouble("stanje") - amount!!.value.toDouble()

            if(racunPlatitelja.stanje > 0){
                var url = "http://20.67.25.104/mBankingAPI/api/account/update.php"
                if( postData(url, gson.toJson(racunPrimatelja)) && postData(url, gson.toJson(racunPlatitelja))){
                    val toast = Toast.makeText(this, "Uspješna transakcija", Toast.LENGTH_SHORT)
                    toast.show()
                }
                else{
                    val toast = Toast.makeText(this, "Transackija nije uspjela", Toast.LENGTH_SHORT)
                    toast.show()
                }

                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val date = Date()
                url = "http://20.67.25.104/mBankingAPI/api/transaction/create.php"
                transaction1 = Transaction(amount!!.value.toDouble(), "Uplata", "HR00", "abc123", formatter.format(date), 3, racunPrimatelja.iban, 1)
                transaction2 = Transaction(amount!!.value.toDouble(), "Isplata", "HR00", "def456", formatter.format(date), 3, racunPlatitelja.iban, 1)
                if(postData(url, gson.toJson(transaction1)) && postData(url, gson.toJson(transaction2))){
                }
                else{
                    val toast = Toast.makeText(this, "Greška pri upisu transakcije u bazu podataka!", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }
    }
}