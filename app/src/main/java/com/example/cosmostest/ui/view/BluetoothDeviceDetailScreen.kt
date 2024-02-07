package com.example.cosmostest.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cosmostest.ui.viewmodel.BleViewModel

@Composable
fun BluetoothDeviceDetailsScreen(navController: NavController, bleViewModel: BleViewModel) {
    val isConnected = bleViewModel.isConnected.collectAsState().value
    val connectedDeviceServices = bleViewModel.connectedDeviceServices.collectAsState().value
    val connectedDevice = bleViewModel.connectedDevice.collectAsState().value

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {
        if (isConnected && connectedDevice != null) {
            Text("Connected", style = MaterialTheme.typography.headlineMedium)
            connectedDeviceServices.forEach { service ->
                Text("Service: ${service.uuid}", style = MaterialTheme.typography.bodyMedium)
                service.characteristics.forEach { characteristic ->
                    Text("Characteristic: ${characteristic.uuid} (Properties: ${characteristic.properties})", style = MaterialTheme.typography.bodySmall)
                }
            }
            Button(onClick = {
                bleViewModel.disconnectFromDevice()
            navController.navigate("bluetooth")}) {
                Text("Disconnect")
            }
        } else {
            Text("Failed to connect to device", style = MaterialTheme.typography.headlineMedium)
        }
        Button(onClick = { navController.navigateUp() }) {
            Text("Back")
        }
    }
}
