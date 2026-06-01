package com.keser.flameguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddSensorDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
  val rooms =
      listOf(
          "Kitchen" to "🍳",
          "Living Room" to "🛋️",
          "Bedroom" to "🛏️",
          "Garage" to "🚗",
          "Basement" to "📦",
      )

  AlertDialog(
      onDismissRequest = onDismiss,
      containerColor = MaterialTheme.colorScheme.surface,
      titleContentColor = MaterialTheme.colorScheme.onBackground,
      title = { Text("Register New Sensor", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text(
              "Select the installation location for your new FireGuard hardware.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
              modifier = Modifier.padding(bottom = 16.dp),
          )
          rooms.forEach { (roomName, emoji) ->
            Row(
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                        .clickable {
                          onAdd(roomName, emoji)
                          onDismiss()
                        }
                        .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(text = emoji, fontSize = 20.sp, modifier = Modifier.padding(end = 12.dp))
              Text(text = roomName, color = MaterialTheme.colorScheme.onBackground)
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = onDismiss) {
          Text("Cancel", color = MaterialTheme.colorScheme.primary)
        }
      },
  )
}
