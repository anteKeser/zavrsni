package com.keser.flameguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BadgePill(text: String, isDanger: Boolean) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme
    val bgColor =
        if (isDanger) colorScheme.error.copy(alpha = 0.1f)
        else colorScheme.onBackground.copy(alpha = 0.04f)
    val borderColor =
        if (isDanger) colorScheme.error.copy(alpha = 0.28f)
        else colorScheme.onBackground.copy(alpha = 0.08f)
    val textColor = if (isDanger) colorScheme.error else colorScheme.onBackground.copy(alpha = 0.6f)

    Row(
        modifier =
            Modifier.clip(RoundedCornerShape(50))
                .background(bgColor)
                .border(1.dp, borderColor, RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isDanger) {
            Box(
                modifier =
                    Modifier.size(6.dp)
                        .clip(CircleShape)
                        .background(colorScheme.error)
                        .padding(end = 6.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text,
            style = typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = textColor
        )
    }
}
