package com.example.cosmostest.ui.viewmodel

import com.example.cosmostest.MainCoroutineRule
import com.example.cosmostest.model.Device
import com.example.cosmostest.repo.IDeviceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DevicesViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var deviceRepository: IDeviceRepository

    private lateinit var devicesViewModel: DevicesViewModel

    @Before
    fun setupViewModel() {
        runBlockingTest {
            whenever(deviceRepository.getDevices()).thenReturn(listOf(
                Device("00:11:22:33:44:55", "Device 1", "RIDE", "2.2.2", "Serial1", null, false, "OFF", false, 0)
            ))
        }
        devicesViewModel = DevicesViewModel(deviceRepository)
    }

    @Test
    fun loadDevices_loadsIntoStateFlow() = mainCoroutineRule.runBlockingTest {
        devicesViewModel.loadDevices()

        assertEquals(listOf(Device("00:11:22:33:44:55",
            "Device 1",
            "RIDE",
            "2.2.2",
            "Serial1",
            null,
            false,
            "OFF",
            false,
            0)), devicesViewModel.devices.first())
    }

    @Test
    fun getDeviceByMacAddress_returnsCorrectDevice() = mainCoroutineRule.runBlockingTest {
        devicesViewModel.loadDevices()

        val device = devicesViewModel.getDeviceByMacAddress("00:11:22:33:44:55")
        assertEquals("Device 1", device?.model)
    }
}
