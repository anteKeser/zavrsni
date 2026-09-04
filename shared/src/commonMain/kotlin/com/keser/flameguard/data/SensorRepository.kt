package com.keser.flameguard.data

import kotlinx.coroutines.flow.Flow

interface SensorRepository {
    fun streamSensorData(): Flow<SensorData>

    suspend fun getHistoricalData(hoursBack: Int): List<SensorData>

    suspend fun clearOldLogs(olderThanDays: Int = 30): Int

    suspend fun getLogCount(): Int
}
