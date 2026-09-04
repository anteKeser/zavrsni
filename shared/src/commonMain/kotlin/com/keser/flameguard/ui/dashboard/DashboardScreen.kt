package com.keser.flameguard.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.keser.flameguard.ui.components.GraphCard
import com.keser.flameguard.ui.components.StatCard
import com.keser.flameguard.ui.components.StatusCard
import kotlin.math.abs
import kotlin.math.round
import org.koin.compose.viewmodel.koinViewModel

private fun Double.formatOneDecimal(): String {
    val rounded = round(this * 10) / 10
    val whole = rounded.toInt()
    val decimalDigit = round(abs(rounded - whole) * 10).toInt()
    return "$whole.$decimalDigit"
}

@Composable
fun DashboardScreen(navController: NavController, viewModel: DashboardViewModel = koinViewModel()) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme
    val state by viewModel.state.collectAsState()

    val sensorData by viewModel.liveSensorData.collectAsState()

    val filters = listOf("1H", "6H", "24H", "7D")

    Scaffold(containerColor = colorScheme.background) { paddingValues ->
        if (sensorData == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorScheme.primary)
            }
        } else {
            Column(
                modifier =
                    Modifier.fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues)
                        .padding(bottom = 48.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                        Text("🔥", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            text = "FireGuard",
                            style = typography.headlineMedium.copy(fontSize = 22.sp),
                            color = colorScheme.onBackground,
                        )
                    }
                    Box(
                        modifier =
                            Modifier.size(38.dp)
                                .clip(CircleShape)
                                .background(colorScheme.onBackground.copy(alpha = 0.05f))
                                .border(
                                    1.dp,
                                    colorScheme.onBackground.copy(alpha = 0.1f),
                                    CircleShape
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = colorScheme.onBackground.copy(alpha = 0.6f),
                        )
                    }
                }

                StatusCard(isDanger = state.isDangerSpike)

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard(
                            title = "SMOKE / CO₂",
                            value = state.smokeLevel.formatOneDecimal(),
                            unit = "ppm",
                            isDanger = false,
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard(
                            title = "GAS LEVEL",
                            value = state.gasLevel.formatOneDecimal(),
                            unit = "ppm",
                            isDanger = state.isDangerSpike,
                        )
                    }
                }

                Row(
                    modifier =
                        Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(colorScheme.onBackground.copy(alpha = 0.05f))
                            .border(
                                1.dp,
                                colorScheme.onBackground.copy(alpha = 0.08f),
                                RoundedCornerShape(50),
                            )
                            .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    filters.forEach { filter ->
                        val isActive = filter == state.activeFilter
                        Box(
                            modifier =
                                Modifier.weight(1f)
                                    .clip(RoundedCornerShape(50))
                                    .background(if (isActive) colorScheme.onBackground else Color.Transparent)
                                    .clickable { viewModel.setTimeFilter(filter) }
                                    .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = filter,
                                style = typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color =
                                    if (isActive) colorScheme.background
                                    else colorScheme.onBackground.copy(alpha = 0.4f),
                            )
                        }
                    }
                }

                GraphCard(
                    title = "SMOKE / CO₂",
                    activeFilter = state.activeFilter,
                    lineColor = Color(0xFF7777EE),
                    isDangerSpike = false,
                    dataPoints = state.historicalData.map { it.smokeLevel.toFloat() },
                )

                GraphCard(
                    title = "GAS LEVEL",
                    activeFilter = state.activeFilter,
                    lineColor = colorScheme.error,
                    isDangerSpike = state.isDangerSpike,
                    dataPoints = state.historicalData.map { it.coLevel.toFloat() },
                )
            }
        }
    }
}
