package com.example.cosmostest.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cosmostest.model.Device
import com.example.cosmostest.ui.viewmodel.DevicesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(device: Device, navController: NavHostController, devicesViewModel: DevicesViewModel) {
    val device = devicesViewModel.getDeviceByMacAddress(device.macAddress)

    if (device != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Device Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Model: ${device.model}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Firmware Version: ${device.firmwareVersion}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Product: ${device.product}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Serial: ${device.serial}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Instalaltion Mode: ${device.installationMode}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                        text = "Brake Light: ${device.brakeLight}",
                style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Light Mode: ${device.lightMode}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Light Auto: ${device.lightAuto}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Light Value: ${device.lightValue}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    } else {
        Text(text = "Device not found")
    }
}
