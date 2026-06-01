package com.keser.flameguard.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FirebaseSensorRepositoryImpl : SensorRepository {

  private val database =
      Firebase.database("https://zavrsni-3f1ea-default-rtdb.europe-west1.firebasedatabase.app")

  private val currentDataRef = database.reference("sensors/device_001/current")
  private val historyDataRef = database.reference("sensors/device_001/history")

  override fun streamSensorData(): Flow<SensorData> {
    return currentDataRef.valueEvents.map { snapshot ->
      val dataMap = snapshot.value as? Map<String, Any> ?: return@map defaultSensorData()

      parseSensorData(dataMap)
    }
  }

  override suspend fun getHistoricalData(hoursBack: Int): List<SensorData> {
    return try {
      val nowMs = Clock.System.now().toEpochMilliseconds()
      val thresholdMs = nowMs - (hoursBack * 60 * 60 * 1000L)

      val snapshot = historyDataRef.orderByKey().startAt(thresholdMs.toString()).valueEvents.first()

      val historyMap = snapshot.value as? Map<String, Map<String, Any>> ?: return emptyList()

      historyMap.values.map { parseSensorData(it) }.sortedBy { it.timestamp }
    } catch (e: Exception) {
      e.printStackTrace()
      emptyList()
    }
  }

  private fun parseSensorData(map: Map<String, Any>): SensorData {
    val temp = map["temperature"].toString().toDoubleOrNull() ?: 0.0
    val smoke = map["smokeLevel"].toString().toDoubleOrNull() ?: 0.0
    val co = map["coLevel"].toString().toDoubleOrNull() ?: 0.0
    val timeMs =
        map["timestamp"].toString().toLongOrNull() ?: Clock.System.now().toEpochMilliseconds()
    val isSafe = map["isSystemSafe"].toString().toBooleanStrictOrNull() ?: true

    return SensorData(
        temperature = temp,
        smokeLevel = smoke,
        coLevel = co,
        timestamp = Instant.fromEpochMilliseconds(timeMs),
        isSystemSafe = isSafe,
    )
  }

  private fun defaultSensorData() =
      SensorData(
          temperature = 0.0,
          smokeLevel = 0.0,
          coLevel = 0.0,
          timestamp = Clock.System.now(),
          isSystemSafe = true,
      )
}
