package hr.foi.air.mbanking.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.foi.air.mbanking.entities.Account
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.entities.User
import hr.foi.air.mbanking.exceptions.HttpRequestFailureException
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class AccountRequest {

    val client = OkHttpClient()

    fun getAllAccounts(): List<Account> {
        val gson = Gson()

        val url = "http://3.72.75.217/mBankingAPI/api/account/get_all.php"
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogreška kod dohvata liste računa!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<Account>>() {}.type
        val accounts = gson.fromJson<List<Account>>(responseString, itemType)

        return accounts
    }

    fun createAccounts(userId: Int): String {
        val iban = "HR" + (100..999).random()
        val jsonObject = JSONObject()
        jsonObject.put("iban", iban)
        jsonObject.put("stanje", 100)
        jsonObject.put("aktivnost", "D")
        jsonObject.put("korisnik_id", userId.toString())
        jsonObject.put("vrsta_racuna_id", 4)
        jsonObject.put("qr_kod", "")

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        val url = "http://3.72.75.217/mBankingAPI/api/account/create.php"
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        return iban
    }

    fun getAccount(id: Int): Account? {
        val gson = Gson()
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("3.72.75.217")
            .addPathSegment("mBankingAPI")
            .addPathSegment("api")
            .addPathSegment("account")
            .addPathSegment("get_all.php")
            .addQueryParameter("id", id.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogrešan unos podataka!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<Account>>() {}.type
        val result = gson.fromJson<List<Account>>(responseString, itemType)
        val account = result[0]

        return account
    }
}