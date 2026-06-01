package com.keser.flameguard.data

import kotlinx.coroutines.flow.Flow

interface SensorRepository {
  fun streamSensorData(): Flow<SensorData>

  suspend fun getHistoricalData(hoursBack: Int): List<SensorData>
}
