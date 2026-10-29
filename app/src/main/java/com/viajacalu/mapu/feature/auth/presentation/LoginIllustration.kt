package com.viajacalu.mapu.feature.auth.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme
import com.viajacalu.mapu.core.designsystem.theme.Ochre

// Paths are in the mock-up's 350x400 coordinate space and scaled to fit
private const val ArtWidth = 350f
private const val ArtHeight = 400f

private val ContourPaths = listOf(
    "M-20 80 C 60 40, 140 120, 220 70 S 360 40, 380 90",
    "M-20 150 C 70 110, 150 190, 240 140 S 360 120, 380 160",
    "M-20 230 C 50 200, 130 270, 220 220 S 350 200, 380 240",
    "M-20 310 C 80 280, 160 350, 250 300 S 360 290, 380 320",
    "M110 -10 L 130 420",
    "M250 -10 L 225 420"
)
private const val RoutePath = "M70 320 C 110 260, 90 210, 150 190 S 230 150, 260 100"
private const val SmallPinPath = "M18 46s-16-14-16-27a16 16 0 1 1 32 0c0 13-16 27-16 27z"
private const val LargePinPath = "M24 60s-21-18-21-36a21 21 0 1 1 42 0c0 18-21 36-21 36z"

/** Decorative map with a dotted route between three pins. Hidden from screen readers. */
@Composable
fun LoginIllustration(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val contours = remember { ContourPaths.map { PathParser().parsePathString(it).toPath() } }
    val route = remember { PathParser().parsePathString(RoutePath).toPath() }
    val smallPin = remember { PathParser().parsePathString(SmallPinPath).toPath() }
    val largePin = remember { PathParser().parsePathString(LargePinPath).toPath() }

    Box(
        modifier = modifier
            .aspectRatio(ArtWidth / ArtHeight)
            .clip(RoundedCornerShape(32.dp))
            .background(colors.surfaceContainer)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            scale(scale = size.width / ArtWidth, pivot = Offset.Zero) {
                contours.forEach { drawPath(it, colors.outlineVariant, style = Stroke(width = 1.5f)) }

                drawPath(
                    path = route,
                    color = colors.primary,
                    style = Stroke(
                        width = 3f,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 9f))
                    )
                )

                drawCircle(color = Ochre.copy(alpha = 0.35f), radius = 34f, center = Offset(300f, 330f))

                drawPin(smallPin, Offset(52f, 286f), colors.secondary, colors.surfaceContainer, Offset(18f, 19f), 6f)
                drawPin(smallPin, Offset(132f, 150f), colors.tertiary, colors.surfaceContainer, Offset(18f, 19f), 6f)
                drawPin(largePin, Offset(236f, 52f), colors.primary, colors.surfaceContainer, Offset(24f, 24f), 8f)
            }
        }
    }
}

private fun DrawScope.drawPin(
    pin: Path,
    position: Offset,
    color: Color,
    holeColor: Color,
    holeCenter: Offset,
    holeRadius: Float
) {
    translate(left = position.x, top = position.y) {
        drawPath(pin, color)
        drawCircle(color = holeColor, radius = holeRadius, center = holeCenter)
    }
}

@Preview
@Composable
private fun LoginIllustrationPreview() {
    MapuTheme {
        LoginIllustration(modifier = Modifier.fillMaxWidth())
    }
}
