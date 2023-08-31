package foi.projekt.skeniraj_i_plati

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import entities.Account
import entities.Transaction
import foi.projekt.skeniraj_i_plati.databinding.LayoutQrcodeBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONTokener
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class QRCodeActivity : AppCompatActivity() {
    private lateinit var binding: LayoutQrcodeBinding
    private lateinit var userId: String
    private lateinit var userIban: String
    private val client = OkHttpClient()
    private val JSON = "application/json; charset=utf-8".toMediaType()
    private lateinit var ibanQRkoda: String
    private lateinit var dataFromQRCode: Array<String>
    private lateinit var transaction1: Transaction
    private lateinit var transaction2: Transaction
    private val userRequest = UserRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val codeData = intent.getStringExtra("QRCodeData").toString()
        userId = intent.getStringExtra("GlavniRacun").toString()
        userIban = intent.getStringExtra("IBAN").toString()
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

    private fun postData(url: String, jsonData: String): Boolean {
        val request = Request.Builder()
            .url(url)
            .post(jsonData.toRequestBody(JSON))
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
                return false
            }
            return true
        }
    }

    private fun onBackArrowPressed() {
        binding.buttonBack.setOnClickListener() {
            finish()
        }
    }

    private fun onOdustaniPressed() {
        binding.buttonOdustani.setOnClickListener() {
            finish()
        }
    }

    private fun onPlatiPressed() {
        binding.buttonPlati.setOnClickListener {
            val dohvacenRacun =
                getData("http://3.72.75.217/mBankingAPI/api/account/get.php?iban=".plus(ibanQRkoda))

            val gson = Gson()
            val racunPrimatelja =
                gson.fromJson(dohvacenRacun.getJSONObject(0).toString(), Account::class.java)
            val racunPlatitelja = userRequest.getUserByIban(userIban)
            val regex = """(\d+)""".toRegex()
            val amount = regex.find(dataFromQRCode[7])
            racunPrimatelja.stanje =
                dohvacenRacun.getJSONObject(0).getDouble("stanje") + amount!!.value.toDouble()
            racunPlatitelja?.stanje =
                racunPlatitelja?.stanje?.minus(amount.value.toDouble()) ?: 100.0

            if (racunPlatitelja?.stanje!! > 0) {
                var url = "http://3.72.75.217/mBankingAPI/api/account/update.php"
                if (postData(url, gson.toJson(racunPrimatelja)) && postData(
                        url,
                        gson.toJson(racunPlatitelja)
                    )
                ) {
                    val toast = Toast.makeText(this, "Uspješna transakcija", Toast.LENGTH_SHORT)
                    toast.show()
                } else {
                    val toast = Toast.makeText(this, "Transackija nije uspjela", Toast.LENGTH_SHORT)
                    toast.show()
                }

                url = "http://3.72.75.217/mBankingAPI/api/transaction/create.php"
                transaction1 = Transaction(
                    platitelj_iban = racunPlatitelja.iban,
                    primatelj_iban = racunPrimatelja.iban,
                    iznos = amount.value.toDouble(),
                    opis_placanja = "placanje skeniraj i plati",
                    model = "HR00",
                    poziv_na_broj = "Neki poziv na broj",
                    datum_izvrsenja = SimpleDateFormat("yyyy-MM-dd").format(Date())
                )

                if (postData(url, gson.toJson(transaction1))
                ) {
                    Toast.makeText(this, "Uspjesno placeno", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Greška pri upisu transakcije", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}