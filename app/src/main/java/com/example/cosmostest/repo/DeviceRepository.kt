package com.example.cosmostest.repo

import com.example.cosmostest.api.CosmoApiService
import com.example.cosmostest.model.Device
import com.example.cosmostest.utils.DataState
import com.example.cosmostest.utils.Logger
import javax.inject.Inject

interface IDeviceRepository {
    suspend fun getDevices(): DataState<List<Device>>
}

class DeviceRepository @Inject constructor(private val cosmosApiService: CosmoApiService) : IDeviceRepository {
    override suspend fun getDevices(): DataState<List<Device>> {
        return try {
            val response = cosmosApiService.fetchDevices()
            if (response.isSuccessful) {
                DataState.Success(response.body()?.devices ?: emptyList())
            } else {
                DataState.Error(Exception("Error fetching devices: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }
}
