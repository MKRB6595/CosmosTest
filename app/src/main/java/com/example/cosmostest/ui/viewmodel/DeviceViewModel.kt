package com.example.cosmostest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmostest.model.Device
import com.example.cosmostest.repo.IDeviceRepository
import com.example.cosmostest.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevicesViewModel @Inject constructor(private val repository: IDeviceRepository) : ViewModel() {
    private val _devicesState = MutableStateFlow<DataState<List<Device>>>(DataState.Loading)
    val devicesState: StateFlow<DataState<List<Device>>> = _devicesState

    init {
        loadDevices()
    }

    fun loadDevices() = viewModelScope.launch {
        _devicesState.value = DataState.Loading
        try {
            val devicesList = repository.getDevices()
            DataState.Success(devicesList)
            _devicesState.value = DataState.Success(devicesList).data
        } catch (e: Exception) {
            _devicesState.value = DataState.Error(e)
        }
    }
    fun getDeviceByMacAddress(macAddress: String): Device? {
        return when (val state = _devicesState.value) {
            is DataState.Success -> state.data.find { it.macAddress == macAddress }
            else -> null
        }
    }

}
