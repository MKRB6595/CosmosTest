package com.example.cosmostest.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class BluetoothDeviceScanner @Inject constructor(private val bluetoothAdapter: BluetoothAdapter) {

    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter.bluetoothLeScanner

    fun startScan(scanCallback: ScanCallback) {
        bluetoothLeScanner?.startScan(scanCallback)
    }

    fun stopScan(scanCallback: ScanCallback) {
        bluetoothLeScanner?.stopScan(scanCallback)
    }
}
