package com.keser.flameguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CheckCircle
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
fun SafeCard(onClick: () -> Unit) {

  val typography = MaterialTheme.typography
  val colorScheme = MaterialTheme.colorScheme
  val safeGreen = Color(0xFF22C55E)

  Box(
      modifier =
          Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .background(colorScheme.onBackground.copy(alpha = 0.04f))
              .border(1.dp, safeGreen.copy(alpha = 0.18f), RoundedCornerShape(20.dp))
              .clickable { onClick() }
              .padding(18.dp)
  ) {
    Column {
      Text("SAFE", style = typography.labelSmall, color = safeGreen)
      Spacer(modifier = Modifier.height(8.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CheckCircle, contentDescription = "Safe", tint = safeGreen)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text("Kitchen Sensor", style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
          Text(
              "ID · FG-001",
              style = typography.labelSmall,
              color = colorScheme.onBackground.copy(alpha = 0.4f),
          )
        }
      }
    }
  }
}
