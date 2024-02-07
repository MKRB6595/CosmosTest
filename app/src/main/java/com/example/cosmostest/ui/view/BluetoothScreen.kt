package com.example.cosmostest.ui.view

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cosmostest.ui.viewmodel.BleViewModel

@Composable
fun BluetoothScreen(navController: NavController, bleViewModel: BleViewModel) {
    val discoveredDevices = bleViewModel.discoveredDevices.collectAsState().value

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Discovered Devices", style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(discoveredDevices) { device ->
                BluetoothDeviceItem(device = device) {
                    bleViewModel.connectToDevice(device.address)
                    navController.navigate("deviceDetails/${device.address}")
                }
            }
        }
    }
}

@Composable
fun BluetoothDeviceItem(device: BluetoothDevice, onConnectClick: (BluetoothDevice) -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onConnectClick(device) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${device.name ?: "Unknown Device"}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Address: ${device.address}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
