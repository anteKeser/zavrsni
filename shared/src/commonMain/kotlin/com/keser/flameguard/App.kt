package com.keser.flameguard

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keser.flameguard.theme.FlameGuardTheme
import com.keser.flameguard.ui.account.AccountScreen
import com.keser.flameguard.ui.dashboard.DashboardScreen
import com.keser.flameguard.ui.home.HomeScreen
import com.keser.flameguard.ui.login.LoginScreen
import com.keser.flameguard.ui.login.RegisterScreen

@Composable
fun App() {
  FlameGuardTheme {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
      composable("login") { LoginScreen(navController = navController) }
      composable("home") { HomeScreen(navController = navController) }
      composable("dashboard") { DashboardScreen(navController = navController) }
      composable("account") { AccountScreen(navController = navController) }
      composable("register") { RegisterScreen(navController = navController) }
    }
  }
}
