package com.keser.flameguard.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.keser.flameguard.ui.components.FireGuardButton
import com.keser.flameguard.ui.components.FireGuardLogoHeader
import com.keser.flameguard.ui.components.FireGuardTextField
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = koinViewModel()) {
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val isLoggingIn = loginState is LoginState.Loading
    val errorMessage = (loginState as? LoginState.Error)?.message

    val backgroundGradient =
        Brush.radialGradient(
            colors =
                listOf(Color(0xFFC05015), Color(0xFF7A2A06), Color(0xFF1A0A02), Color(0xFF06020A)),
            radius = 1500f,
        )

    Box(
        modifier = Modifier.fillMaxSize().background(backgroundGradient).padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, Color(0x2EFF6E1E), RoundedCornerShape(28.dp))
                    .padding(horizontal = 28.dp, vertical = 40.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FireGuardLogoHeader()

                FireGuardTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        viewModel.clearError()
                    },
                    label = "EMAIL",
                    placeholder = "your@email.com",
                )

                FireGuardTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        viewModel.clearError()
                    },
                    label = "PASSWORD",
                    placeholder = "••••••••",
                    isPassword = true,
                    showPassword = showPassword,
                    onTogglePassword = { showPassword = !showPassword },
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                FireGuardButton(
                    text = if (isLoggingIn) "Authenticating..." else "Sign In",
                    onClick = {
                        viewModel.performLogin(
                            email = email,
                            password = password,
                            onSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            },
                        )
                    },
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Text(
                        text = "Don't have an account? ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f),
                        fontSize = 13.sp,
                    )
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { navController.navigate("register") },
                    )
                }
            }
        }
    }
}
