package hr.foi.air.mbanking.api

import hr.foi.air.mbanking.exceptions.HttpRequestFailureException
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UserRequest {
    val client = OkHttpClient()

    fun getAllUsers(): JSONArray {
        var users = JSONArray()
        val url = "http://20.67.25.104/mBankingAPI/api/user/get_all.php";
        val request = Request.Builder()
                      .url(url)
                      .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                users = JSONArray(responseString)
            }

            override fun onFailure(call: Call, e: IOException) {
                throw HttpRequestFailureException(e.message.toString())
            }
        })

        return users
    }

    fun getUser(id: Int) : String {
        var user = JSONObject()
        var name = ""
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("20.67.25.104")
            .addPathSegment("mBankingAPI")
            .addPathSegment("api")
            .addPathSegment("user")
            .addPathSegment("get_all.php")
            .addQueryParameter("id", id.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                user = JSONObject(responseString)
                name = user.getString("ime")
            }

            override fun onFailure(call: Call, e: IOException) {
                throw HttpRequestFailureException(e.message.toString())
            }
        })

        return name
    }

}