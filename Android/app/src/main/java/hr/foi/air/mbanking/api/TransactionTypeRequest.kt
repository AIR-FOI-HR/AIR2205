package hr.foi.air.mbanking.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.foi.air.mbanking.entities.TransactionType
import hr.foi.air.mbanking.exceptions.HttpRequestFailureException
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class TransactionTypeRequest {

    val client = OkHttpClient()

    fun getAllTransactionTypes(): List<TransactionType>{
        val gson = Gson()

        val url = "http://3.72.75.217/mBankingAPI/api/transaction_type/get_all.php"
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogreška kod dohvata liste transakcija!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<TransactionType>>() {}.type
        val currencies = gson.fromJson<List<TransactionType>>(responseString, itemType)

        return currencies
    }
    fun getTransactionType(id: Int) : TransactionType? {
        val gson = Gson()
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("3.72.75.217")
            .addPathSegment("mBankingAPI")
            .addPathSegment("api")
            .addPathSegment("transaction_type")
            .addPathSegment("get_all.php")
            .addQueryParameter("id", id.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogrešan unos podataka!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<TransactionType>>() {}.type
        val result = gson.fromJson<List<TransactionType>>(responseString, itemType)
        val currency = result[0]

        return currency
    }
}