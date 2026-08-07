package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun SpatialPatternView(
    questionId: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .clip(RoundedCornerShape(16.dp))
            .testTag("spatial_pattern_view"),
        color = Color(0xFF1E293B),
        tonalElevation = 6.dp
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(16.dp)) {
            Canvas(modifier = Modifier.fillMaxWidth()) {
                val width = size.width
                val height = size.height
                val navyColor = Color(0xFF00BCD4) // Cyan accent
                val goldColor = Color(0xFFFFD700) // Gold
                val strokeWidth = 5f

                when (questionId % 4) {
                    0 -> {
                        // Rotated Matrix Arrows
                        val boxSize = width / 4f
                        for (i in 0..2) {
                            val cx = (i + 0.8f) * boxSize
                            val cy = height / 2f
                            val angle = i * 90f

                            // Draw box
                            drawRoundRect(
                                color = Color(0xFF334155),
                                topLeft = Offset(cx - boxSize / 2f, cy - boxSize / 2f),
                                size = Size(boxSize, boxSize),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                            )

                            // Draw Arrow
                            val path = Path().apply {
                                moveTo(cx, cy - boxSize * 0.3f)
                                lineTo(cx + boxSize * 0.2f, cy)
                                lineTo(cx + boxSize * 0.08f, cy)
                                lineTo(cx + boxSize * 0.08f, cy + boxSize * 0.3f)
                                lineTo(cx - boxSize * 0.08f, cy + boxSize * 0.3f)
                                lineTo(cx - boxSize * 0.08f, cy)
                                lineTo(cx - boxSize * 0.2f, cy)
                                close()
                            }
                            drawPath(
                                path = path,
                                color = if (i == 2) goldColor else navyColor
                            )
                        }
                    }
                    1 -> {
                        // 3D Isometric Cube Stacking
                        val cx = width / 2f
                        val cy = height / 2f
                        val sizeR = height * 0.25f

                        // Draw Isometric Cubes
                        val pathTop = Path().apply {
                            moveTo(cx, cy - sizeR)
                            lineTo(cx + sizeR, cy - sizeR / 2f)
                            lineTo(cx, cy)
                            lineTo(cx - sizeR, cy - sizeR / 2f)
                            close()
                        }
                        val pathRight = Path().apply {
                            moveTo(cx, cy)
                            lineTo(cx + sizeR, cy - sizeR / 2f)
                            lineTo(cx + sizeR, cy + sizeR / 2f)
                            lineTo(cx, cy + sizeR)
                            close()
                        }
                        val pathLeft = Path().apply {
                            moveTo(cx, cy)
                            lineTo(cx - sizeR, cy - sizeR / 2f)
                            lineTo(cx - sizeR, cy + sizeR / 2f)
                            lineTo(cx, cy + sizeR)
                            close()
                        }

                        drawPath(pathTop, color = Color(0xFF38BDF8))
                        drawPath(pathRight, color = Color(0xFF0284C7))
                        drawPath(pathLeft, color = Color(0xFF0369A1))

                        // Grid Outline
                        drawPath(pathTop, color = Color.White, style = Stroke(width = 3f))
                        drawPath(pathRight, color = Color.White, style = Stroke(width = 3f))
                        drawPath(pathLeft, color = Color.White, style = Stroke(width = 3f))
                    }
                    2 -> {
                        // Reflection Pattern F vs Mirror
                        val cx = width / 2f
                        val cy = height / 2f

                        // Center Mirror line
                        drawLine(
                            color = goldColor,
                            start = Offset(cx, cy - height * 0.4f),
                            end = Offset(cx, cy + height * 0.4f),
                            strokeWidth = 6f
                        )

                        // Left Shape 'F'
                        val fPathLeft = Path().apply {
                            moveTo(cx - 80f, cy - 60f)
                            lineTo(cx - 30f, cy - 60f)
                            moveTo(cx - 80f, cy - 60f)
                            lineTo(cx - 80f, cy + 60f)
                            moveTo(cx - 80f, cy)
                            lineTo(cx - 40f, cy)
                        }
                        drawPath(fPathLeft, color = navyColor, style = Stroke(width = 10f))

                        // Right Shape '?'
                        drawCircle(
                            color = goldColor,
                            radius = 35f,
                            center = Offset(cx + 60f, cy),
                            style = Stroke(width = 4f)
                        )
                    }
                    else -> {
                        // Geometric Matrix
                        val margin = width / 6f
                        for (r in 0..1) {
                            for (c in 0..2) {
                                val px = margin * (c + 1) * 1.5f
                                val py = height * (r + 1) * 0.35f
                                if (r == 0) {
                                    drawCircle(color = navyColor, radius = 25f, center = Offset(px, py))
                                } else {
                                    drawRect(
                                        color = if (c == 2) goldColor else Color(0xFF94A3B8),
                                        topLeft = Offset(px - 22f, py - 22f),
                                        size = Size(44f, 44f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
