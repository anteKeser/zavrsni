package com.keser.flameguard.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.keser.flameguard.ui.components.AddSensorDialog
import com.keser.flameguard.ui.components.BadgePill
import com.keser.flameguard.ui.components.DangerCard
import com.keser.flameguard.ui.components.OfflineCard
import com.keser.flameguard.ui.components.SafeCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
  val typography = MaterialTheme.typography
  val colorScheme = MaterialTheme.colorScheme
  val state by viewModel.state.collectAsState()
  var showAddDialog by remember { mutableStateOf(false) }

  val backgroundGradient =
      Brush.radialGradient(
          colors = listOf(Color(0x478C280A), Color.Transparent),
          radius = 1200f,
          center = androidx.compose.ui.geometry.Offset(800f, 0f),
      )

  Scaffold(
      containerColor = colorScheme.background,
      floatingActionButton = {
        if (state.devices.size < 5) {
          FloatingActionButton(
              onClick = { showAddDialog = true },
              containerColor = colorScheme.primary,
              contentColor = colorScheme.onPrimary,
              shape = CircleShape,
          ) {
            Icon(Icons.Default.Add, contentDescription = "Add Device")
          }
        }
      },
  ) { paddingValues ->
    Box(modifier = Modifier.fillMaxSize().background(backgroundGradient).padding(paddingValues)) {
      Column(
          modifier =
              Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 100.dp)
      ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 22.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🔥", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
            Text(
                text = "FireGuard",
                style = typography.headlineMedium.copy(fontSize = 22.sp),
                color = colorScheme.onBackground,
            )
          }
          Box(
              modifier =
                  Modifier.size(38.dp)
                      .clip(CircleShape)
                      .background(colorScheme.onBackground.copy(alpha = 0.05f))
                      .border(1.dp, colorScheme.onBackground.copy(alpha = 0.1f), CircleShape)
                      .clickable { navController.navigate("account") },
              contentAlignment = Alignment.Center,
          ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile",
                tint = colorScheme.onBackground.copy(alpha = 0.6f),
            )
          }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
          Text(
              text = "Hello, ${state.userName} 👋",
              style = typography.headlineLarge,
              color = colorScheme.onBackground,
              modifier = Modifier.padding(bottom = 8.dp),
          )
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val hasWarnings = state.activeWarnings > 0
            BadgePill(
                text = if (hasWarnings) "${state.activeWarnings} WARNING ACTIVE" else "0 WARNINGS",
                isDanger = hasWarnings,
            )

            BadgePill(text = "${state.devices.size} DEVICES", isDanger = false)
          }
        }

        Text(
            text = "CONNECTED SENSORS",
            style = typography.labelSmall,
            color = colorScheme.onBackground.copy(alpha = 0.4f),
            modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 12.dp),
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          state.devices.forEach { device ->
            Box(modifier = Modifier.fillMaxWidth()) {
              if (device.isOnline) {
                if (state.isSystemSafe) {
                  SafeCard(onClick = { navController.navigate("dashboard") })
                } else {
                  DangerCard(onClick = { navController.navigate("dashboard") })
                }
              } else {
                OfflineCard()
              }

              IconButton(
                  onClick = { viewModel.removeDevice(device.id) },
                  modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
              ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove Device",
                    tint = colorScheme.onBackground.copy(alpha = 0.3f),
                    modifier = Modifier.size(20.dp),
                )
              }
            }
          }
        }

        HorizontalDivider(
            color = colorScheme.onBackground.copy(alpha = 0.07f),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 28.dp),
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
          Row(
              modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Column {
              Text(
                  "Data Management",
                  style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
              )
              Text(
                  "LOCAL DATABASE",
                  style = typography.labelSmall,
                  color = colorScheme.onBackground.copy(alpha = 0.4f),
              )
            }
            Text(
                text = if (state.logsCleared) "0 logs" else "${state.logCount} logs",
                style = typography.labelSmall,
                modifier =
                    Modifier.clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.onBackground.copy(alpha = 0.04f))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
            )
          }

          Box(
              modifier =
                  Modifier.fillMaxWidth()
                      .clip(RoundedCornerShape(20.dp))
                      .background(colorScheme.surface.copy(alpha = 0.5f))
                      .border(
                          1.dp,
                          colorScheme.onBackground.copy(alpha = 0.07f),
                          RoundedCornerShape(20.dp),
                      )
                      .padding(18.dp)
          ) {
            Column {
              Text(
                  text =
                      "Removes all sensor log entries older than 30 days. This action is permanent.",
                  style = typography.labelSmall.copy(letterSpacing = 0.sp),
                  color = colorScheme.onBackground.copy(alpha = 0.6f),
                  modifier = Modifier.padding(bottom = 14.dp),
              )

              Row(
                  modifier =
                      Modifier.fillMaxWidth()
                          .clip(RoundedCornerShape(14.dp))
                          .background(colorScheme.error.copy(alpha = 0.07f))
                          .border(
                              1.dp,
                              colorScheme.error.copy(alpha = 0.22f),
                              RoundedCornerShape(14.dp),
                          )
                          .clickable(enabled = !state.logsCleared && !state.isClearingLogs) {
                            viewModel.clearOldLogs()
                          }
                          .padding(vertical = 13.dp),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Clear",
                    tint = colorScheme.error.copy(alpha = 0.9f),
                    modifier = Modifier.size(16.dp).padding(end = 8.dp),
                )
                Text(
                    text =
                        if (state.isClearingLogs) "Clearing..."
                        else if (state.logsCleared) "✓ Logs Cleared" else "Clear Old Logs",
                    style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.error.copy(alpha = 0.9f),
                )
              }
            }
            if (showAddDialog) {
              AddSensorDialog(
                  onDismiss = { showAddDialog = false },
                  onAdd = { roomName, emoji -> viewModel.addDevice(roomName, emoji) },
              )
            }
          }
        }
      }
    }
  }
}
