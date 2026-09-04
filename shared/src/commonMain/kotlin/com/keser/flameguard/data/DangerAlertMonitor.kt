package com.keser.flameguard.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DangerAlertMonitor(
    sensorRepository: SensorRepository,
    private val settingsRepository: SettingsRepository,
    private val notificationSender: NotificationSender,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) {
    private var wasDanger = false

    init {
        sensorRepository.streamSensorData()
            .onEach { data ->
                val isDanger = data.isDanger
                if (isDanger && !wasDanger && settingsRepository.notificationsEnabled.value) {
                    notificationSender.send(
                        title = "⚠️ Danger Detected",
                        body = "Sensor readings exceeded safe thresholds.",
                    )
                }
                wasDanger = isDanger
            }
            .catch { it.printStackTrace() }
            .launchIn(scope)
    }
}
