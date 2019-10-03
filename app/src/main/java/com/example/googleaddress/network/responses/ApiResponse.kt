package com.example.googleaddress.network.responses

import com.google.gson.annotations.SerializedName

data class ApiResponse (
    @SerializedName("address") val address: String,
    @SerializedName("createdon") val createdon: String,
    @SerializedName("id") val id: Int,
    @SerializedName("rating") val rating: Any?,
    @SerializedName("source") val source: String
)
