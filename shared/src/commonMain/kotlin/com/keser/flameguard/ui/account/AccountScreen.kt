package com.keser.flameguard.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountScreen(navController: NavController, viewModel: AccountViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val backgroundGradient =
        Brush.radialGradient(
            colors = listOf(Color(0x478C280A), Color.Transparent),
            radius = 1200f,
            center = androidx.compose.ui.geometry.Offset(800f, 0f),
        )

    Scaffold(containerColor = colorScheme.background) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().background(backgroundGradient).padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(32.dp).padding(end = 8.dp),
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colorScheme.onBackground,
                        )
                    }
                    Text(
                        text = "Account",
                        style = typography.headlineMedium.copy(fontSize = 22.sp),
                        color = colorScheme.onBackground,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier =
                        Modifier.size(100.dp)
                            .clip(CircleShape)
                            .background(colorScheme.surface)
                            .border(2.dp, colorScheme.primary.copy(alpha = 0.5f), CircleShape)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(colorScheme.onBackground.copy(alpha = 0.05f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.size(50.dp),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = state.userName,
                    style = typography.headlineLarge.copy(fontSize = 28.sp),
                    color = colorScheme.onBackground,
                )
                Text(
                    text = state.email,
                    style = typography.bodyMedium,
                    color = colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 4.dp, bottom = 48.dp),
                )

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorScheme.surface.copy(alpha = 0.5f))
                            .border(
                                1.dp,
                                colorScheme.onBackground.copy(alpha = 0.07f),
                                RoundedCornerShape(20.dp),
                            )
                ) {
                    Column {
                        AccountToggleRow(
                            icon = Icons.Default.DarkMode,
                            title = "Dark Mode",
                            checked = state.isDarkMode,
                            onCheckedChange = viewModel::toggleDarkMode,
                        )
                        HorizontalDivider(color = colorScheme.onBackground.copy(alpha = 0.05f))
                        AccountToggleRow(
                            icon = Icons.Default.Notifications,
                            title = "Notifications",
                            checked = state.notificationsEnabled,
                            onCheckedChange = viewModel::toggleNotifications,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(bottom = 32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(colorScheme.error.copy(alpha = 0.05f))
                            .border(
                                1.dp,
                                colorScheme.error.copy(alpha = 0.3f),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                viewModel.signOut {
                                    navController.navigate("login") { popUpTo(0) }
                                }
                            }
                            .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sign Out",
                            style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = colorScheme.error,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccountToggleRow(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
            modifier = Modifier.weight(1f),
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
