package com.keser.flameguard.data

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val isDarkMode: StateFlow<Boolean>
    val notificationsEnabled: StateFlow<Boolean>
    fun setDarkMode(enabled: Boolean)
    fun setNotificationsEnabled(enabled: Boolean)
}
