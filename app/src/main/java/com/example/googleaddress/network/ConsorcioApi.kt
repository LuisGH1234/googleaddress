package com.example.googleaddress.network

import android.util.Log
import com.example.googleaddress.network.responses.ApiResponse
import com.example.googleaddress.network.services.IApiService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConsorcioApi {
    companion object {
        private const val TAG = "ConsorcioApi"
        private const val baseUrl = "https://analytics.consorciohbo.com.pe"

        // Es un accessor por lo que no se esta definiendo el request, sino que
        // cada vez que lo llamen se ejecutara y creara el request con retrofit2.
        // Los request tienen un tiempo de vida corto y no se crea uno por Api
        private val request get() = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private fun onError(t: Throwable) {
            t.printStackTrace()
        }

        fun getShipment(onSuccess: (Response<ApiResponse>) -> Unit,
                        onErr: (Throwable) -> Unit = ::onError) {
            val service = request.create(IApiService::class.java)
            val call = service.getShipment()

            call.enqueue(object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    onErr(t)
                }

                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.code() != 404)
                        onSuccess(response)
                    else Log.e(TAG, "404 - Not Found - ${response.message()}")
                }
            })
        }
    }
}