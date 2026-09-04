package com.keser.flameguard

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {
  private val requestNotificationPermission =
      registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* respected either way */ }

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    setContent { App() }
  }
}
