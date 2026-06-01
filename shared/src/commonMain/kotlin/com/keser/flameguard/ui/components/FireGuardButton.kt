package com.keser.flameguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FireGuardButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
  val typography = MaterialTheme.typography
  val colorScheme = MaterialTheme.colorScheme
  val gradient = Brush.linearGradient(colors = listOf(colorScheme.primary, colorScheme.secondary))

  Box(
      modifier =
          modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(gradient)
              .clickable { onClick() }
              .padding(vertical = 16.dp),
      contentAlignment = Alignment.Center,
  ) {
    Text(
        text = text,
        style = typography.bodyLarge,
        color = colorScheme.onPrimary,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.3.sp,
    )
  }
}
