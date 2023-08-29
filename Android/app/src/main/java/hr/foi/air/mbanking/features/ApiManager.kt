package hr.foi.air.mbanking.features

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import hr.foi.air.mbanking.exceptions.HttpRequestFailureException
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object ApiManager {
    private val client = OkHttpClient()
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private fun buildApiUrl(
        vararg pathSegments: String,
        queryParams: Map<String, Any>? = null
    ): HttpUrl {
        val baseUrl = HttpUrl.Builder()
            .scheme("http")
            .host(IP_ADDRESS)
            .addPathSegment("mBankingAPI")
            .addPathSegment("api")

        for (segment in pathSegments) {
            baseUrl.addPathSegment(segment)
        }

        queryParams?.let {
            for ((key, value) in queryParams) {
                baseUrl.addQueryParameter(key, value.toString())
            }
        }

        return baseUrl.build()
    }


    fun <T> makeApiCall(
        vararg pathSegments: String,
        queryParams: Map<String, Any>? = null,
        postBody: Map<String, Any>? = null,
        returnType: TypeToken<T>
    ): Result<T> {

        val url = buildApiUrl(*pathSegments, queryParams = queryParams)

        val requestBody = postBody?.let { gson.toJson(it) }

        val request = Request.Builder()
            .url(url)
            .post((requestBody ?: "").toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                return Result.failure(HttpRequestFailureException("Pogrešan unos podataka!"))
            }

            val responseString = response.body?.string()

            val result = gson.fromJson<T>(responseString, returnType.type)

            return Result.success(result)
        } catch (e: IOException) {
            return Result.failure(e)
        } catch (e: JsonParseException) {
            return Result.failure(HttpRequestFailureException("JSON parsing error: " + e.message))
        }
    }

    private const val IP_ADDRESS = "3.72.75.217"
}