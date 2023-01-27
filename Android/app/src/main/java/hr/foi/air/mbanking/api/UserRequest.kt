package hr.foi.air.mbanking.api


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.foi.air.mbanking.entities.User
import hr.foi.air.mbanking.exceptions.HttpRequestFailureException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


class UserRequest {
    val client = OkHttpClient()

    fun getAllUsers(): List<User> {
        val gson = Gson()

        val url = "http://20.67.25.104/mBankingAPI/api/user/get_all.php";
        val request = Request.Builder()
                      .url(url)
                      .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogreška kod dohvata liste korisnika!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<User>>() {}.type
        val users = gson.fromJson<List<User>>(responseString, itemType)

        return users
    }

    fun getUser(id: Int) : User {
        val gson = Gson()
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

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogrešan unos podataka!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<User>>() {}.type
        val result = gson.fromJson<List<User>>(responseString, itemType)
        val user = result[0]

        return user
    }

    fun createUser(
        ime: String,
        prezime: String,
        email: String,
        pin: String,
    ): String? {
        val jsonObject = JSONObject()
        jsonObject.put("ime", ime)
        jsonObject.put("prezime", prezime)
        jsonObject.put("email", email)
        jsonObject.put("adresa", "")
        jsonObject.put("mobitel", "")
        jsonObject.put("pin", pin)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val url = "http://20.67.25.104/mBankingAPI/api/user/create.php";
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseString = response.body?.string()
        val jsonResponse = JSONArray(responseString)
        val recoveryCode = jsonResponse.getJSONObject(0).getString("kod_za_oporavak")

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogreška kod slanja podataka")

        return recoveryCode
    }

}