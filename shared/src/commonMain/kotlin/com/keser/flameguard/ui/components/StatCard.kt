package com.keser.flameguard.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatCard(title: String, value: String, unit: String, isDanger: Boolean) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    val safeGreen = Color(0xFF22C55E)
    val statusColor = if (isDanger) colorScheme.error else safeGreen

    Box(
        modifier =
            Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(colorScheme.surface.copy(alpha = if (isDanger) 0.6f else 0.3f))
                .border(
                    1.dp,
                    if (isDanger) colorScheme.error.copy(alpha = 0.2f)
                    else colorScheme.onBackground.copy(alpha = 0.05f),
                    RoundedCornerShape(18.dp),
                )
                .padding(18.dp)
    ) {
        Column {
            Text(
                text = title,
                style = typography.labelSmall,
                color = colorScheme.onBackground.copy(alpha = 0.4f),
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Text(
                text = value,
                style =
                    typography.labelSmall.copy(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-2).sp,
                    ),
                color = if (isDanger) colorScheme.error else colorScheme.onBackground,
            )
            Text(
                text = unit,
                style = typography.labelSmall,
                color = colorScheme.onBackground.copy(alpha = 0.3f),
                modifier = Modifier.padding(top = 2.dp),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp),
            ) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(statusColor))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isDanger) "DANGER" else "SAFE",
                    style = typography.labelSmall,
                    color = statusColor,
                )
            }
        }
    }
}
