package com.example.cosmostest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmostest.model.Device
import com.example.cosmostest.repo.IDeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevicesViewModel @Inject constructor(private val repository: IDeviceRepository) : ViewModel() {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices : StateFlow<List<Device>> = _devices
    init {
        loadDevices()
    }

    fun loadDevices() {
        viewModelScope.launch {
            _devices.value = repository.getDevices() ?: emptyList()
        }
    }
    fun getDeviceByMacAddress(macAddress: String): Device? {
        return devices.value.find { it.macAddress == macAddress }
    }

}
