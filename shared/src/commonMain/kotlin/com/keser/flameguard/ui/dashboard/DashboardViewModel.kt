package com.keser.flameguard.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keser.flameguard.data.SensorData
import com.keser.flameguard.data.SensorRepository
import com.keser.flameguard.data.isDanger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardState(
    val smokeLevel: Double = 0.0,
    val gasLevel: Double = 0.0,
    val activeFilter: String = "1H",
    val isDangerSpike: Boolean = false,
    val isSystemSafe: Boolean = true,
    val historicalData: List<SensorData> = emptyList(),
)

class DashboardViewModel(private val sensorRepository: SensorRepository) : ViewModel() {

  private val _state = MutableStateFlow(DashboardState())
  val state: StateFlow<DashboardState> = _state.asStateFlow()

  init {
    fetchHistoricalData("1H")
  }

  val liveSensorData: StateFlow<SensorData?> =
      sensorRepository
          .streamSensorData()
          .onEach { incomingData ->
            _state.update { currentState ->
              currentState.copy(
                  smokeLevel = incomingData.smokeLevel,
                  gasLevel = incomingData.coLevel,
                  isSystemSafe = incomingData.isSystemSafe,
                  isDangerSpike = incomingData.isDanger,
              )
            }
          }
          .stateIn(
              scope = viewModelScope,
              started = SharingStarted.WhileSubscribed(5000),
              initialValue = null,
          )

  fun setTimeFilter(newFilter: String) {
    if (_state.value.activeFilter == newFilter) return
    _state.update { it.copy(activeFilter = newFilter) }
    fetchHistoricalData(newFilter)
  }

  private fun fetchHistoricalData(filter: String) {
    viewModelScope.launch {
      val hoursBack =
          when (filter) {
            "1H" -> 1
            "6H" -> 6
            "24H" -> 24
            "7D" -> 7 * 24
            else -> 1
          }

      val history = sensorRepository.getHistoricalData(hoursBack)
      _state.update { it.copy(historicalData = history) }
    }
  }
}
