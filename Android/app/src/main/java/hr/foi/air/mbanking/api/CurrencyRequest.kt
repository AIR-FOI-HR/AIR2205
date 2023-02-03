package hr.foi.air.mbanking.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.foi.air.mbanking.entities.Currency
import hr.foi.air.mbanking.exceptions.HttpRequestFailureException
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class CurrencyRequest {

    val client = OkHttpClient()

    fun getAllCurrencies(): List<Currency>{
        val gson = Gson()

        val url = "http://3.72.75.217/mBankingAPI/api/currency/get_all.php"
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogreška kod dohvata liste transakcija!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<Currency>>() {}.type
        val currencies = gson.fromJson<List<Currency>>(responseString, itemType)

        return currencies
    }
    fun getCurrency(id: Int) : Currency? {
        val gson = Gson()
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("3.72.75.217")
            .addPathSegment("mBankingAPI")
            .addPathSegment("api")
            .addPathSegment("currency")
            .addPathSegment("get_all.php")
            .addQueryParameter("id", id.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogrešan unos podataka!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<Currency>>() {}.type
        val result = gson.fromJson<List<Currency>>(responseString, itemType)
        val currency = result[0]

        return currency
    }
}