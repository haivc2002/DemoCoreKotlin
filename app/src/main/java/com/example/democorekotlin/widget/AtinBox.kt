package com.example.democorekotlin.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * ## A container with rounded corners and gradient background.
 *
 * Supports up to **2 colors** for a horizontal linear gradient.
 * If no colors are provided, falls back to [MaterialTheme.colorScheme.primary].
 *
 * @param modifier Modifier for the outer container.
 * @param padding Inner padding (default: 8.dp all sides).
 * @param radius Corner radius (default: 18.dp).
 * @param colors Gradient colors (max 2). Pass `null` or empty to use theme primary color.
 * @param content Composable content inside the box.
 *
 * ### Usage:
 * ```kotlin
 * // Default — uses theme primary color
 * AtinBox {
 *     Text("Hello", color = Color.White)
 * }
 *
 * // Single color
 * AtinBox(colors = listOf(Color.Red)) {
 *     Text("Error", color = Color.White)
 * }
 *
 * // Gradient with 2 colors
 * AtinBox(
 *     colors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5)),
 *     radius = 24.dp,
 *     padding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
 * ) {
 *     Text("Gradient Box", color = Color.White)
 * }
 *
 * // Custom modifier
 * AtinBox(
 *     modifier = Modifier.fillMaxWidth(),
 *     colors = listOf(Color.Blue, Color.Cyan)
 * ) {
 *     Row {
 *         Icon(Icons.Default.Star, contentDescription = null)
 *         Text("Featured")
 *     }
 * }
 * ```
 */
@Composable
fun AtinBox(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(10.dp),
    radius: Dp = 18.dp,
    colors: List<Color>? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    require(colors == null || colors.size <= 2) { "max color 2 item" }

    val primaryColor = MaterialTheme.colorScheme.primary
    val resolvedColors = when {
        colors.isNullOrEmpty() -> listOf(primaryColor, primaryColor)
        colors.size == 1 -> listOf(colors[0], colors[0])
        else -> colors
    }

    val shape = RoundedCornerShape(radius)

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.horizontalGradient(resolvedColors),
                shape = shape
            )
            .padding(padding),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}