package foi.projekt.skeniraj_i_plati

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class UserRequest {

    val client = OkHttpClient()

    fun getUser(id: Int) : User {
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

        if (!response.isSuccessful) throw  Exception("Pogrešan unos podataka!")

        val responseString = response.body?.string()
        val itemType = object : TypeToken<List<User>>() {}.type
        val result = gson.fromJson<List<User>>(responseString, itemType)
        val user = result[0]

        return user
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
            result.getOrNull()?.get(0)
        } else {
            null
        }
    }

}