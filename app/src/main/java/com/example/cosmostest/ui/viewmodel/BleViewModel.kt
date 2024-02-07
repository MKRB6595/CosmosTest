package com.example.cosmostest.ui.viewmodel

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmostest.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BleViewModel @Inject constructor(
    @ApplicationContext private val context: Context) : ViewModel() {
    private val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var bluetoothGatt: BluetoothGatt? = null

    private val _discoveredDevices = MutableStateFlow(emptyList<BluetoothDevice>())
    val discoveredDevices : StateFlow<List<BluetoothDevice>> = _discoveredDevices
    private val _connectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    val connectedDevice : StateFlow<BluetoothDevice?> = _connectedDevice

    private val _isConnected = MutableStateFlow(false)
    val isConnected : StateFlow<Boolean> = _isConnected

    private val _connectedDeviceServices = MutableStateFlow<List<BluetoothGattService>>(emptyList())
    val connectedDeviceServices: StateFlow<List<BluetoothGattService>> = _connectedDeviceServices

    private val _bleState = MutableStateFlow<DataState<Boolean>>(DataState.Loading)
    val bleState: StateFlow<DataState<Boolean>> = _bleState
    init {
        checkBluetoothEnabled()
    }
    fun checkBluetoothEnabled() = viewModelScope.launch {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        _bleState.value = if (bluetoothAdapter?.isEnabled == true) {
            DataState.Success(true)
        } else {
            DataState.Error(Exception("Bluetooth est désactivé"))
        }
    }
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val isNewDevice = discoveredDevices.value.none { it.address == device.address }
            if (isNewDevice) {
                viewModelScope.launch(Dispatchers.Main) {
                    val newList = discoveredDevices.value.toMutableList().apply {
                        add(device)
                    }
                    _discoveredDevices.value = newList
                }
            }
        }
    }


    fun startScanning() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            bluetoothAdapter?.bluetoothLeScanner?.startScan(scanCallback)
            delay(10000)
            stopScanning()
        }
    }

    fun stopScanning() {
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
    }

    fun connectToDevice(address: String) {
        val device = bluetoothAdapter?.getRemoteDevice(address) ?: return
        bluetoothGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices()
                    _isConnected.value = true
                    _connectedDevice.value = device
                }else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    _isConnected.value = false
                    _connectedDevice.value = null
                    _connectedDeviceServices.value = emptyList()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    viewModelScope.launch(Dispatchers.Main) {
                        _connectedDeviceServices.value = gatt.services
                    }
                }
            }
        })
    }
    fun disconnectFromDevice() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        viewModelScope.launch(Dispatchers.Main) {
            _isConnected.value = false
            _connectedDevice.value = null
            _connectedDeviceServices.value = emptyList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothGatt?.close()
    }

}
