package hr.foi.air.mbanking.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.exceptions.HttpRequestFailureException
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class TransactionRequest {

    val client = OkHttpClient()

    fun getAllTransactions(): List<Transaction>{
        val gson = Gson()

        val url = "http://20.67.25.104/mBankingAPI/api/transaction/get_all.php"
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogreška kod dohvata liste transakcija!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<Transaction>>() {}.type
        val transactions = gson.fromJson<List<Transaction>>(responseString, itemType)

        return transactions
    }
    fun getTransaction(id: Int) : Transaction? {
        val gson = Gson()
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("20.67.25.104")
            .addPathSegment("mBankingAPI")
            .addPathSegment("api")
            .addPathSegment("transaction")
            .addPathSegment("get_all.php")
            .addQueryParameter("id", id.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw HttpRequestFailureException("Pogrešan unos podataka!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<Transaction>>() {}.type
        val result = gson.fromJson<List<Transaction>>(responseString, itemType)
        val transaction = result[0]

        return transaction
    }
}