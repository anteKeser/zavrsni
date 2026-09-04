package com.keser.flameguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FireGuardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onTogglePassword: () -> Unit = {},
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(
            text = label,
            style = typography.labelSmall,
            color = colorScheme.onBackground.copy(alpha = 0.3f),
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.onBackground.copy(alpha = 0.05f))
                    .border(
                        1.dp,
                        colorScheme.primary.copy(alpha = 0.18f),
                        RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle =
                    typography.bodyLarge.copy(
                        color = colorScheme.onBackground,
                        fontSize = 14.sp,
                    ),
                cursorBrush = SolidColor(colorScheme.primary),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
                    ),
                visualTransformation =
                    if (isPassword && !showPassword) PasswordVisualTransformation()
                    else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = typography.bodyLarge,
                                    color = colorScheme.onBackground.copy(alpha = 0.28f),
                                    fontSize = 13.sp,
                                )
                            }
                            innerTextField()
                        }
                        if (isPassword) {
                            IconButton(
                                onClick = onTogglePassword,
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Toggle Password",
                                    tint = colorScheme.onBackground.copy(alpha = 0.3f),
                                )
                            }
                        }
                    }
                },
            )
        }
    }
}
