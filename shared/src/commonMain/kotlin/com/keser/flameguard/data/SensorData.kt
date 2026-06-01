package com.keser.flameguard.data

import kotlin.time.Instant

data class SensorData(
    val temperature: Double,
    val smokeLevel: Double,
    val coLevel: Double,
    val timestamp: Instant,
    val isSystemSafe: Boolean,
)
