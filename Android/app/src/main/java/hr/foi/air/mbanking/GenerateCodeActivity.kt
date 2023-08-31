package hr.foi.air.mbanking

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import hr.foi.air.mbanking.api.UserRequest
import hr.foi.air.mbanking.databinding.LayoutGenerateCodeBinding
import hr.foi.air.mbanking.entities.User
import hr.foi.air.mbanking.features.domain.models.UserAccount
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
    private lateinit var korisnikId: String
    private lateinit var korisnikIban: String
    private var user: User? = null
    private var userAccount: UserAccount? = null
    private val JSON = "application/json; charset=utf-8".toMediaType()
    private val userRequest = UserRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutGenerateCodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        korisnikId = intent.getStringExtra("GlavniRacun").toString()
        korisnikIban = intent.getStringExtra("IBAN").toString()
        user = userRequest.getUser(korisnikId.toInt())
        userAccount = userRequest.getUserByIban(korisnikIban)

        generateQrCode()
        onBackArrowPressed()
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

            var sadrzaj = "BCD\n001\n1\nSCT\nBSFTWB\n"
                .plus(user?.ime)
                .plus("\n")
                .plus(userAccount?.iban)
                .plus("\nEUR")
                .plus(binding.textAmount.text)
                .plus("\nCHAR\n")
                .plus(binding.textPaymentReference.text)
                .plus("\nNesta\nEPC QR Code")


            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(sadrzaj, BarcodeFormat.QR_CODE, 512, 512)
            binding.imageQrCode.setImageBitmap(bitmap)

            val qrCodeString = bitmapToString(bitmap)
            userAccount?.qr_kod  = qrCodeString
            if(postData("http://3.72.75.217/mBankingAPI/api/account/update.php", gson.toJson(userAccount))){
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