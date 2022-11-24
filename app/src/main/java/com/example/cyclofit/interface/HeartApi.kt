package com.example.cyclofit.`interface`

import com.example.cyclofit.model.HeartRate
import retrofit2.http.GET

interface HeartApi {

    @GET("1.json?results=10")
    suspend fun getHeartRate(): HeartRate

}