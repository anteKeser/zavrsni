package com.keser.flameguard.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keser.flameguard.data.AuthRepository
import com.keser.flameguard.data.SensorRepository
import com.keser.flameguard.data.isDanger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SensorDevice(
    val id: String,
    val name: String,
    val roomEmoji: String,
    val isOnline: Boolean,
    val isPrimary: Boolean = false,
)

data class HomeState(
    val userName: String = "User",
    val activeWarnings: Int = 0,
    val logCount: Int = 0,
    val isClearingLogs: Boolean = false,
    val logsCleared: Boolean = false,
    val isSystemSafe: Boolean = true,
    val devices: List<SensorDevice> =
        listOf(SensorDevice("FG-001", "Kitchen Sensor", "🍳", isOnline = true, isPrimary = true)),
)

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val sensorRepository: SensorRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        val email = authRepository.currentUser?.email ?: "User"
        val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        _state.update { it.copy(userName = name) }

        viewModelScope.launch {
            sensorRepository.streamSensorData().collect { incomingData ->
                _state.update { currentState ->
                    val isDanger = incomingData.isDanger
                    currentState.copy(
                        activeWarnings = if (isDanger) 1 else 0,
                        isSystemSafe = !isDanger
                    )
                }
            }
        }

        viewModelScope.launch {
            val count = sensorRepository.getLogCount()
            _state.update { it.copy(logCount = count) }
        }
    }

    fun addDevice(roomName: String, emoji: String) {
        val currentDevices = _state.value.devices
        if (currentDevices.size >= 5) return

        val newId = "FG-00${(2..9).random()}"
        val newDevice =
            SensorDevice(id = newId, name = "$roomName Sensor", roomEmoji = emoji, isOnline = false)

        _state.update { it.copy(devices = currentDevices + newDevice) }
    }

    fun removeDevice(deviceId: String) {
        _state.update { currentState ->
            currentState.copy(devices = currentState.devices.filter { it.id != deviceId })
        }
    }

    fun clearOldLogs() {
        if (_state.value.isClearingLogs) return
        viewModelScope.launch {
            _state.update { it.copy(isClearingLogs = true) }
            sensorRepository.clearOldLogs(olderThanDays = 30)
            val remaining = sensorRepository.getLogCount()
            _state.update {
                it.copy(
                    isClearingLogs = false,
                    logsCleared = true,
                    logCount = remaining
                )
            }
        }
    }
}
