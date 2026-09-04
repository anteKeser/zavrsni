package com.keser.flameguard.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keser.flameguard.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun performLogin(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            authRepository
                .login(email, password)
                .fold(
                    onSuccess = {
                        _loginState.value = LoginState.Idle
                        onSuccess()
                    },
                    onFailure = { e ->
                        _loginState.value =
                            LoginState.Error(
                                e.message ?: "Authentication failed. Please try again."
                            )
                    },
                )
        }
    }

    fun clearError() {
        _loginState.value = LoginState.Idle
    }
}
