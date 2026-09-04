package com.keser.flameguard.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val KEY_DARK_MODE = "dark_mode"
private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"

class SettingsRepositoryImpl(private val settings: Settings) : SettingsRepository {

    private val _isDarkMode = MutableStateFlow(settings.getBoolean(KEY_DARK_MODE, true))
    override val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _notificationsEnabled =
        MutableStateFlow(settings.getBoolean(KEY_NOTIFICATIONS_ENABLED, true))
    override val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    override fun setDarkMode(enabled: Boolean) {
        settings.putBoolean(KEY_DARK_MODE, enabled)
        _isDarkMode.value = enabled
    }

    override fun setNotificationsEnabled(enabled: Boolean) {
        settings.putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
        _notificationsEnabled.value = enabled
    }
}
