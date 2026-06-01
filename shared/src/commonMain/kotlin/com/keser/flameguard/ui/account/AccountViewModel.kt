package com.keser.flameguard.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keser.flameguard.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountState(
    val email: String = "",
    val userName: String = "",
    val isLoggingOut: Boolean = false
)

class AccountViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    init {
        val userEmail = authRepository.currentUser?.email ?: "No Email Found"
        val name = userEmail.substringBefore("@").replaceFirstChar { it.uppercase() }

        _state.update { it.copy(email = userEmail, userName = name) }
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