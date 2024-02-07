package com.example.cosmostest.model

data class Device(
    val macAddress: String,
    val model: String,
    val product: String?,
    val firmwareVersion: String,
    val serial: String,
    val installationMode: String? = null,
    val brakeLight: Boolean,
    val lightMode: String,
    val lightAuto: Boolean,
    val lightValue: Int
)
