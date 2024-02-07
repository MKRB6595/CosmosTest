package com.example.cosmostest

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cosmostest.ui.view.BluetoothDeviceDetailsScreen
import com.example.cosmostest.ui.view.BluetoothScreen
import com.example.cosmostest.ui.view.DeviceDetailScreen
import com.example.cosmostest.ui.view.DeviceListScreen
import com.example.cosmostest.ui.viewmodel.BleViewModel
import com.example.cosmostest.ui.viewmodel.DevicesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val devicesViewModel: DevicesViewModel = viewModel()
    val bleViewModel: BleViewModel = viewModel()
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                bleViewModel.startScanning()
                navController.navigate("bluetooth")
            } else {
                Toast.makeText(context, "Permissions Bluetooth requises", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(navController = navController, onBluetoothScanClick = {
                    requestPermissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN

                    ))
                })
            }
            composable("deviceList") {
                DeviceListScreen(navController = navController, devicesViewModel)
            }
            composable(
                "deviceDetail/{macAddress}",
                arguments = listOf(navArgument("macAddress") { type = NavType.StringType })
            ) { backStackEntry ->
                val macAddress = backStackEntry.arguments?.getString("macAddress") ?: ""
                devicesViewModel.getDeviceByMacAddress(macAddress)
                    ?.let { DeviceDetailScreen(device = it, navController = navController, devicesViewModel ) }
            }
            composable("bluetooth") {
                BluetoothScreen(navController = navController, bleViewModel )
            }
            composable("deviceDetails/{deviceAddress}", arguments = listOf(navArgument("deviceAddress") { type = NavType.StringType })) { backStackEntry ->
                BluetoothDeviceDetailsScreen(
                    navController = navController,
                    bleViewModel = bleViewModel
                )
            }
        }
    }
}
@Composable
fun HomeScreen(navController: NavController, onBluetoothScanClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { navController.navigate("deviceList") }) {
            Text("Voir les appareils")
        }
        Button(onClick = onBluetoothScanClick) {
            Text("Scanner les appareils Bluetooth")
        }
    }
}
