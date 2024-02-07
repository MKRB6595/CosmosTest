package com.example.cosmostest.ble

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class BluetoothManager @Inject constructor(context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? = (context.getSystemService(BLUETOOTH_SERVICE) as BluetoothAdapter?)

    fun isBluetoothSupported(): Boolean = bluetoothAdapter != null

    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled ?: false

}
