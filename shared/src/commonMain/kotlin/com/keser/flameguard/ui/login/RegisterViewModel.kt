package com.keser.flameguard.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keser.flameguard.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
  object Idle : RegisterState()

  object Loading : RegisterState()

  data class Error(val message: String) : RegisterState()

  object Success : RegisterState()
}

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

  private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
  val state: StateFlow<RegisterState> = _state.asStateFlow()

  fun performRegister(email: String, password: String, confirmPass: String, onSuccess: () -> Unit) {
    if (email.isBlank() || password.isBlank()) {
      _state.value = RegisterState.Error("Fields cannot be empty.")
      return
    }
    if (password != confirmPass) {
      _state.value = RegisterState.Error("Passwords do not match.")
      return
    }
    if (password.length < 6) {
      _state.value = RegisterState.Error("Password must be at least 6 characters.")
      return
    }

    _state.value = RegisterState.Loading
    viewModelScope.launch {
      authRepository
          .register(email.trim(), password)
          .fold(
              onSuccess = {
                _state.value = RegisterState.Success
                onSuccess()
              },
              onFailure = { e ->
                _state.value = RegisterState.Error(e.message ?: "Registration failed.")
              },
          )
    }
  }

  fun resetState() {
    _state.value = RegisterState.Idle
  }
}
