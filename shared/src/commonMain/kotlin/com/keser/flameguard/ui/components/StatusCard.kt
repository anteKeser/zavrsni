package com.keser.flameguard.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusCard(isDanger: Boolean = false) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by
    infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
    )

    val statusColor = if (isDanger) colorScheme.error else Color(0xFF4CAF50)
    val bgAlpha = if (isDanger) 0.2f * pulseAlpha else 0.1f
    val borderAlpha = if (isDanger) 0.5f * pulseAlpha else 0.2f

    Box(
        modifier =
            Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(statusColor.copy(alpha = bgAlpha))
                .border(
                    1.dp,
                    statusColor.copy(alpha = borderAlpha),
                    RoundedCornerShape(22.dp),
                )
                .padding(vertical = 30.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 14.dp),
            ) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isDanger) "SYSTEM ALERT" else "SYSTEM STATUS",
                    style = typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.onBackground.copy(alpha = 0.65f),
                )
            }
            Text(
                text = if (isDanger) "WARNING" else "SAFE",
                style = typography.headlineLarge.copy(fontSize = 32.sp),
                color = colorScheme.onBackground,
            )
            Text(
                text = if (isDanger) "HIGH GAS DETECTED" else "ALL SENSORS NOMINAL",
                style = typography.headlineMedium.copy(fontSize = 18.sp),
                color = statusColor,
                modifier = Modifier.padding(top = 6.dp),
            )
            Row(
                modifier =
                    Modifier.padding(top = 18.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.3f))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = if (isDanger) Icons.Default.Warning else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(12.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isDanger) "EVACUATE IMMEDIATELY" else "MONITORING ACTIVE",
                    style = typography.labelSmall,
                    color = colorScheme.onBackground.copy(alpha = 0.8f),
                )
            }
        }
    }
}
