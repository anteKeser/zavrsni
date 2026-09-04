package com.keser.flameguard.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraphCard(
    title: String,
    activeFilter: String,
    lineColor: Color,
    isDangerSpike: Boolean,
    dataPoints: List<Float> = emptyList(),
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    val textMeasurer = rememberTextMeasurer()
    val axisTextStyle =
        TextStyle(color = colorScheme.onBackground.copy(alpha = 0.4f), fontSize = 10.sp)

    val maxNodes = 15
    val displayData =
        if (dataPoints.size > maxNodes) {
            val chunkSize = dataPoints.size / maxNodes
            dataPoints.chunked(chunkSize).map { chunk -> chunk.average().toFloat() }
        } else {
            dataPoints
        }

    Box(
        modifier =
            Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(colorScheme.surface.copy(alpha = if (isDangerSpike) 0.6f else 0.3f))
                .border(
                    1.dp,
                    if (isDangerSpike) colorScheme.error.copy(alpha = 0.2f)
                    else colorScheme.onBackground.copy(alpha = 0.05f),
                    RoundedCornerShape(18.dp),
                )
                .padding(top = 18.dp, bottom = 10.dp)
    ) {
        Column {
            Row(modifier = Modifier.padding(start = 18.dp, end = 18.dp, bottom = 12.dp)) {
                Text(
                    text = title,
                    style = typography.labelSmall,
                    color = colorScheme.onBackground.copy(alpha = 0.5f),
                )
                Text(
                    text = " — $activeFilter",
                    style = typography.labelSmall,
                    color = colorScheme.onBackground.copy(alpha = 0.2f),
                )
            }

            Canvas(
                modifier =
                    Modifier.fillMaxWidth().height(160.dp)
                        .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                val width = size.width
                val height = size.height

                val bottomPadding = 20f
                val graphHeight = height - bottomPadding

                val maxDataValue = (displayData.maxOrNull() ?: 1f).coerceAtLeast(10f)
                val yAxisMax = maxDataValue * 1.2f

                val limitY = graphHeight * 0.3f
                drawLine(
                    color = colorScheme.error.copy(alpha = 0.5f),
                    start = Offset(0f, limitY),
                    end = Offset(width, limitY),
                    strokeWidth = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f),
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = yAxisMax.toInt().toString(),
                    topLeft = Offset(0f, 0f),
                    style = axisTextStyle,
                )
                drawText(
                    textMeasurer = textMeasurer,
                    text = "0",
                    topLeft = Offset(0f, graphHeight - 15f),
                    style = axisTextStyle,
                )

                if (displayData.size < 2) return@Canvas

                val stepX = width / (displayData.size - 1).coerceAtLeast(1).toFloat()

                val path = Path()
                var previousX = 0f
                var previousY = graphHeight

                displayData.forEachIndexed { index, value ->
                    val x = index * stepX
                    val normalizedY = (value / yAxisMax).coerceIn(0f, 1f)
                    val y = graphHeight - (normalizedY * graphHeight)

                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        val controlPointX = (previousX + x) / 2
                        path.cubicTo(controlPointX, previousY, controlPointX, y, x, y)
                    }
                    previousX = x
                    previousY = y
                }

                val fillPath =
                    Path().apply {
                        addPath(path)
                        lineTo(width, graphHeight)
                        lineTo(0f, graphHeight)
                        close()
                    }

                drawPath(
                    path = fillPath,
                    brush =
                        Brush.verticalGradient(
                            colors = listOf(lineColor.copy(alpha = 0.3f), Color.Transparent),
                            startY = 0f,
                            endY = graphHeight,
                        ),
                )

                drawPath(path = path, color = lineColor, style = Stroke(width = 5f))
            }
        }
    }
}
