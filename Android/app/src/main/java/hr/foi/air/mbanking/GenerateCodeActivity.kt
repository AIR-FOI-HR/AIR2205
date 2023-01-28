package hr.foi.air.mbanking

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import hr.foi.air.mbanking.databinding.LayoutGenerateCodeBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.ByteArrayOutputStream
import java.io.IOException

class GenerateCodeActivity: AppCompatActivity() {

    private lateinit var binding: LayoutGenerateCodeBinding
    private val client = OkHttpClient()
    private lateinit var glavniRacun: JSONObject
    private val JSON = "application/json; charset=utf-8".toMediaType()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutGenerateCodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        glavniRacun = JSONObject(intent.getStringExtra("GlavniRacun").toString())

        generateQrCode()
        onBackArrowPressed()
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
                return false
            }
            return true
        }
    }

    private fun bitmapToString(bitmap: Bitmap): String{
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun generateQrCode(){
        binding.buttonGenerateQrCode.setOnClickListener{
            var gson = Gson()
            var racunKorisnika = gson.fromJson(glavniRacun.toString(), hr.foi.air.mbanking.entities.Account::class.java)

            var korisnik = getData("http://20.67.25.104/mBankingAPI/api/user/get.php?id=".plus(racunKorisnika.korisnik_id))
            var sadrzaj = "BCD\n001\n1\nSCT\nBSFTWB\n"
                .plus(korisnik.getJSONObject(0).getString("ime"))
                .plus("\n")
                .plus(racunKorisnika.iban)
                .plus("\nEUR")
                .plus(binding.textAmount.text)
                .plus("\nCHAR\n")
                .plus(binding.textPaymentReference.text)
                .plus("\nNesta\nEPC QR Code")


            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(sadrzaj, BarcodeFormat.QR_CODE, 512, 512)
            binding.imageQrCode.setImageBitmap(bitmap)

            val qrCodeString = bitmapToString(bitmap)
            racunKorisnika.qr_kod = qrCodeString
            if(postData("http://20.67.25.104/mBankingAPI/api/account/update.php", gson.toJson(racunKorisnika))){
                val toast = Toast.makeText(this, "Spremljeno", Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                val toast = Toast.makeText(this, "Greška pri upisu QR koda u bazu podataka!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun onBackArrowPressed(){
        binding.buttonBack.setOnClickListener(){
            finish()
        }
    }
}