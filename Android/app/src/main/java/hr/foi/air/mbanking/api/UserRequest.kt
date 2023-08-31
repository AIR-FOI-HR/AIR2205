package hr.foi.air.mbanking.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.foi.air.mbanking.entities.User
import hr.foi.air.mbanking.exceptions.HttpRequestFailureException

import hr.foi.air.mbanking.exceptions.LoginFailureException
import hr.foi.air.mbanking.features.ApiManager
import hr.foi.air.mbanking.features.domain.models.UserAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class UserRequest {

    val client = OkHttpClient()

    fun getAllUsers(): List<User> {
        val gson = Gson()

        val url = "http://3.72.75.217/mBankingAPI/api/user/get_all.php"
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


    fun getUser(id: Int) : User? {
        val gson = Gson()
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("3.72.75.217")
            .addPathSegment("mBankingAPI")
            .addPathSegment("api")
            .addPathSegment("user")
            .addPathSegment("get.php")
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


    fun logInUser(email: String, pin: String) : User? {

        val gson = Gson()
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("pin", pin)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://3.72.75.217/mBankingAPI/api/user/login.php")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw LoginFailureException("Pogrešan email ili PIN!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<User>>() {}.type
        val result = gson.fromJson<List<User>>(responseString, itemType)
        val user = result[0]

        return user
    }

    fun updateUser(korisnik_id: Int, ime: String, prezime: String, email: String, adresa: String, mobitel: String, pin: String, kod_za_oporavak: String): Int{
        val jsonObject = JSONObject()
        jsonObject.put("korisnik_id", korisnik_id)
        jsonObject.put("ime", ime)
        jsonObject.put("prezime", prezime)
        jsonObject.put("email", email)
        jsonObject.put("adresa", adresa)
        jsonObject.put("mobitel", mobitel)
        jsonObject.put("pin", pin)
        jsonObject.put("kod_za_oporavak", kod_za_oporavak)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val url = "http://3.72.75.217/mBankingAPI/api/user/update.php"

        val request = Request.Builder()
            .url(url)
            .put(requestBody)
            .build()

        val response = client.newCall(request).execute()

        if(!response.isSuccessful)
            throw HttpRequestFailureException("Pogreška kod slanja podataka")

        return response.code
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

        val url = "http://3.72.75.217/mBankingAPI/api/user/create.php";
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

    fun getUserByIban(userIban: String): UserAccount? {
        val queryParams = mapOf("iban" to userIban)
        val userAccountsType = object : TypeToken<List<UserAccount>>() {}
        val result = ApiManager.makeApiCall(
            pathSegments = arrayOf("account", "get.php"),
            queryParams = queryParams,
            returnType = userAccountsType
        )
        return if (result.isSuccess) {
            Log.d("LOLOLO", result.toString())
            result.getOrNull()?.get(0)
        } else {
            null
        }
    }

}