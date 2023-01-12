package hr.foi.air.mbanking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import okhttp3.*
import java.io.IOException

class LogInActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val client = OkHttpClient()
        val request = Request.Builder().url("http://20.67.25.104/mBankingAPI/api/transaction/get_all.php").build()

        client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    // Do something with the response
                    val responseString = response.body?.string()
                    Toast.makeText(this, responseString, Toast.LENGTH_LONG).show();
                }

                override fun onFailure(call: Call, e: IOException) {
                    // Handle failure
                }
            })





    }
}