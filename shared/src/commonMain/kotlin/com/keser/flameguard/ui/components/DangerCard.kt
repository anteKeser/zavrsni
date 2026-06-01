package com.keser.flameguard.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DangerCard(onClick: () -> Unit) {
  val typography = MaterialTheme.typography
  val colorScheme = MaterialTheme.colorScheme

  val infiniteTransition = rememberInfiniteTransition()
  val pulseAlpha by
      infiniteTransition.animateFloat(
          initialValue = 0.12f,
          targetValue = 0.22f,
          animationSpec =
              infiniteRepeatable(
                  animation = tween(1100, easing = LinearEasing),
                  repeatMode = RepeatMode.Reverse,
              ),
      )

  Box(
      modifier =
          Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .background(colorScheme.error.copy(alpha = pulseAlpha))
              .border(1.dp, colorScheme.error.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
              .clickable { onClick() }
              .padding(18.dp)
  ) {
    Column {
      Text("WARNING", style = typography.labelSmall, color = colorScheme.error)
      Spacer(modifier = Modifier.height(8.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Warning, contentDescription = "Warning", tint = colorScheme.error)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text("Garage Sensor", style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
          Text(
              "ID · FG-002",
              style = typography.labelSmall,
              color = colorScheme.onBackground.copy(alpha = 0.4f),
          )
        }
      }
    }
  }
}
