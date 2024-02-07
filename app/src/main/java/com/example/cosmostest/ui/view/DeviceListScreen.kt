package com.example.cosmostest.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cosmostest.model.Device
import com.example.cosmostest.ui.viewmodel.DevicesViewModel
import com.example.cosmostest.utils.DataState

@Composable
fun DeviceListScreen(navController: NavController, devicesViewModel: DevicesViewModel) {
    val state = devicesViewModel.devicesState.collectAsState().value

    when (state) {
        is DataState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        is DataState.Success -> {
            DeviceList(state.data, navController = navController)
        }
        is DataState.Error -> {
            Text("Erreur de chargement : ${state.exception.message}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp))
        }
    }

}

@Composable
fun DeviceList(device: List<Device>, navController: NavController){
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(device) { device ->
            DeviceItem(device = device) {
                navController.navigate("deviceDetail/${device.macAddress}")
            }
        }
    }
}

@Composable
fun DeviceItem(device: Device, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = "${device.model} - ${device.firmwareVersion}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}