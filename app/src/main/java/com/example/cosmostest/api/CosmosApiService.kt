package com.example.cosmostest.api
import com.example.cosmostest.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface CosmoApiService {
    @GET("test/devices")
    suspend fun fetchDevices(): Response<ApiResponse>
}
