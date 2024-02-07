package com.example.cosmostest.repo

import com.example.cosmostest.api.CosmoApiService
import com.example.cosmostest.model.Device
import com.example.cosmostest.utils.Logger
import javax.inject.Inject

interface IDeviceRepository {
    suspend fun getDevices(): List<Device>?
}
class DeviceRepository @Inject constructor(private val cosmosApiService: CosmoApiService) : IDeviceRepository{
    override suspend fun getDevices(): List<Device>? {
        try {
            val response = cosmosApiService.fetchDevices()
            if (response.isSuccessful) {
                return response.body()?.devices
            } else {
                Logger.e("DeviceRepository", "Error fetching devices: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Logger.e("DeviceRepository", "Exception fetching devices", e)
        }
        return null
    }

}
