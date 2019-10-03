package com.example.googleaddress.network.services

import com.example.googleaddress.network.responses.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface IApiService {
    @Headers("Accept: application/json")
    @GET("servicio/api/entregas")
    fun getShipment(): Call<ApiResponse>
}