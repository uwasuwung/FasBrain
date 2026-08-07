package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun IshiharaPlateView(
    questionId: Int,
    modifier: Modifier = Modifier
) {
    // Generate deterministic pseudo-random dots for Ishihara plate based on questionId
    val dots = remember(questionId) {
        val random = Random(questionId * 7919)
        val list = mutableListOf<DotData>()
        val greenColors = listOf(
            Color(0xFF43A047), Color(0xFF388E3C), Color(0xFF2E7D32),
            Color(0xFF66BB6A), Color(0xFF81C784), Color(0xFF1B5E20)
        )
        val redOrangeColors = listOf(
            Color(0xFFE53935), Color(0xFFD81B60), Color(0xFFF4511E),
            Color(0xFFFB8C00), Color(0xFFFF7043), Color(0xFFC62828)
        )
        val backgroundColors = listOf(
            Color(0xFF8D6E63), Color(0xFFA1887F), Color(0xFFBCAAA4),
            Color(0xFF78909C), Color(0xFF90A4AE)
        )

        // Generate dots inside unit circle (0 to 1 range)
        for (i in 0..280) {
            val radius = Math.sqrt(random.nextDouble()).toFloat() * 0.9f
            val angle = random.nextDouble() * 2 * Math.PI
            val x = (radius * Math.cos(angle)).toFloat()
            val y = (radius * Math.sin(angle)).toFloat()
            val dotRadius = random.nextFloat() * 0.035f + 0.015f

            // Shape mask for number pattern (e.g. 12, 8, 29, 5)
            val isNumberRegion = when (questionId % 5) {
                0 -> (Math.abs(x) < 0.15f && y > -0.5f && y < 0.5f) || (x in -0.3f..0.3f && Math.abs(y - 0.5f) < 0.1f) // "12" or "I"
                1 -> (x * x + y * y in 0.15f..0.35f && x > 0) || (x * x + y * y in 0.05f..0.2f && x < 0) // "8"
                2 -> (x in -0.4f..0.0f && y < 0) || (x in 0.0f..0.4f && y > 0) // "29"
                3 -> (x in -0.3f..0.3f && Math.abs(y) < 0.15f) || (x < -0.1f && y < 0) // "5"
                else -> (Math.abs(x + y) < 0.2f) // Path line
            }

            val color = if (isNumberRegion) {
                redOrangeColors[random.nextInt(redOrangeColors.size)]
            } else {
                if (random.nextBoolean()) greenColors[random.nextInt(greenColors.size)]
                else backgroundColors[random.nextInt(backgroundColors.size)]
            }

            list.add(DotData(x, y, dotRadius, color))
        }
        list
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(CircleShape)
            .testTag("ishihara_plate_view"),
        color = Color(0xFF263238),
        tonalElevation = 8.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val center = Offset(canvasWidth / 2f, canvasHeight / 2f)
                val plateRadius = canvasWidth / 2f

                dots.forEach { dot ->
                    val cx = center.x + dot.x * plateRadius
                    val cy = center.y + dot.y * plateRadius
                    val r = dot.radius * plateRadius
                    drawCircle(
                        color = dot.color,
                        radius = r,
                        center = Offset(cx, cy)
                    )
                }
            }
        }
    }
}

private data class DotData(
    val x: Float,
    val y: Float,
    val radius: Float,
    val color: Color
)
