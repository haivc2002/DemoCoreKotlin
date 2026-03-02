package com.example.democorekotlin.widget

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.democorekotlin.res.MyColor
import kotlinx.coroutines.delay

@Composable
fun AtinDonutChart(
    modifier: Modifier = Modifier,
    data: Map<Color, Double>,
    size: Dp = 140.dp,
    strokeWidth: Dp = 20.dp,
    backgroundColor: Color = MyColor.sliver
) {
    var start by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        start = true
    }

    val animatedAngle by animateFloatAsState(
        targetValue = if (start) (2 * Math.PI).toFloat() else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            drawDonutChart(
                data = data,
                strokeWidth = strokeWidth.toPx(),
                backgroundColor = backgroundColor,
                animatedAngle = animatedAngle
            )
        }
    }
}

private fun DrawScope.drawDonutChart(
    data: Map<Color, Double>,
    strokeWidth: Float,
    backgroundColor: Color,
    animatedAngle: Float
) {
    val tau = (2 * Math.PI).toFloat()
    val startAngle = -tau / 4
    drawArc(
        color = backgroundColor,
        startAngle = Math.toDegrees(startAngle.toDouble()).toFloat(),
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(width = strokeWidth)
    )

    var consumedAngle = 0f
    fun drawSegment(color: Color, segmentAngle: Float) {
        if (animatedAngle <= consumedAngle) return
        val visibleAngle =
            (animatedAngle - consumedAngle).coerceIn(0f, segmentAngle)
        val startDeg = Math.toDegrees((startAngle + consumedAngle).toDouble()).toFloat()
        val sweepDeg = Math.toDegrees(visibleAngle.toDouble()).toFloat()
        drawArc(
            color = color.copy(alpha = 0.5f),
            startAngle = startDeg,
            sweepAngle = sweepDeg,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Butt
            )
        )
        drawArc(
            color = color,
            startAngle = startDeg,
            sweepAngle = sweepDeg,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Butt
            )
        )
    }

    data.forEach { (color, percent) ->
        if (percent <= 0) return@forEach
        drawSegment(
            color,
            (percent / 100f * tau).toFloat()
        )
    }
}