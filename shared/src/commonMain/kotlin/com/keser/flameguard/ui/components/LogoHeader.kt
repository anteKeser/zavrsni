package com.keser.flameguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FireGuardLogoHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(bottom = 36.dp),
    ) {
        val typography = MaterialTheme.typography
        val colorScheme = MaterialTheme.colorScheme
        Box(
            modifier =
                Modifier.size(72.dp)
                    .clip(CircleShape)
                    .background(colorScheme.primary.copy(alpha = 0.12f))
                    .border(1.dp, colorScheme.primary.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text("🔥", fontSize = 32.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "FireGuard",
            style = typography.headlineMedium,
            color = colorScheme.onBackground,
        )

        Text(
            text = "IOT SAFETY SYSTEM",
            style = typography.labelSmall,
            color = colorScheme.onBackground.copy(alpha = 0.25f),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
