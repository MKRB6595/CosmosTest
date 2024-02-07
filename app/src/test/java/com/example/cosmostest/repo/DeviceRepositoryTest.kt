package com.example.cosmostest.repo

import com.example.cosmostest.MainCoroutineRule
import com.example.cosmostest.api.CosmoApiService
import com.example.cosmostest.model.ApiResponse
import com.example.cosmostest.model.Device
import com.example.cosmostest.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DeviceRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var cosmoApiService: CosmoApiService

    private lateinit var deviceRepository: DeviceRepository

    @Before
    fun setup() {
        deviceRepository = DeviceRepository(cosmoApiService)
    }

    @Test
    fun getDevices_returnsDeviceList() = mainCoroutineRule.runBlockingTest {
        val mockDeviceList = listOf(Device("00:11:22:33:44:55",
            "Device 1",
            "RIDE",
            "2.2.2",
            "Serial1",
            null,
            false,
            "OFF",
            false,
            0))
        val mockApiResponse = ApiResponse(mockDeviceList)
        val mockResponse = Response.success(mockApiResponse)

        `when`(cosmoApiService.fetchDevices()).thenReturn(mockResponse)

        val result = deviceRepository.getDevices()

        assert(result is DataState.Success)
        assertEquals(mockDeviceList, (result as DataState.Success).data)

    }

    @Test
    fun getDevices_returnsNullOnError() = mainCoroutineRule.runBlockingTest {

        val mockResponse = Response.error<ApiResponse>(404, okhttp3.ResponseBody.create(null, ""))

        `when`(cosmoApiService.fetchDevices()).thenReturn(mockResponse)

        val result = deviceRepository.getDevices()

        assert(result is DataState.Error)
    }
}
