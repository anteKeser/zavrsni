package com.keser.flameguard.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keser.flameguard.data.AuthRepository
import com.keser.flameguard.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountState(
    val email: String = "",
    val userName: String = "",
    val isLoggingOut: Boolean = false,
    val isDarkMode: Boolean = true,
    val notificationsEnabled: Boolean = true,
)

class AccountViewModel(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    init {
        val userEmail = authRepository.currentUser?.email ?: "No Email Found"
        val name = userEmail.substringBefore("@").replaceFirstChar { it.uppercase() }

        _state.update { it.copy(email = userEmail, userName = name) }

        viewModelScope.launch {
            settingsRepository.isDarkMode.collect { isDarkMode ->
                _state.update { it.copy(isDarkMode = isDarkMode) }
            }
        }
        viewModelScope.launch {
            settingsRepository.notificationsEnabled.collect { notificationsEnabled ->
                _state.update { it.copy(notificationsEnabled = notificationsEnabled) }
            }
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        settingsRepository.setDarkMode(enabled)
    }

    fun toggleNotifications(enabled: Boolean) {
        settingsRepository.setNotificationsEnabled(enabled)
    }

    fun signOut(onSignOutComplete: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoggingOut = true) }
            authRepository.logout()
            _state.update { it.copy(isLoggingOut = false) }
            onSignOutComplete()
        }
    }
}
