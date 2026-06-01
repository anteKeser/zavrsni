package com.keser.flameguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OfflineCard() {
  val typography = MaterialTheme.typography
  val colorScheme = MaterialTheme.colorScheme
  val offlineGray = Color(0xFF555577)

  Box(
      modifier =
          Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .background(colorScheme.onBackground.copy(alpha = 0.02f))
              .border(1.dp, colorScheme.onBackground.copy(alpha = 0.07f), RoundedCornerShape(20.dp))
              .padding(18.dp)
  ) {
    Column {
      Text("OFFLINE", style = typography.labelSmall, color = offlineGray)
      Spacer(modifier = Modifier.height(8.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Build, contentDescription = "Offline", tint = offlineGray)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
              "Basement Sensor",
              style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
              color = offlineGray,
          )
          Text("ID · FG-003", style = typography.labelSmall, color = offlineGray)
        }
      }
    }
  }
}
