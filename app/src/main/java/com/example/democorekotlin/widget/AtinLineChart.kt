package com.example.democorekotlin.widget

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.democorekotlin.res.MyColor
import kotlinx.coroutines.delay

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AtinLineChart(
    data: Map<Color, Double>,
    modifier: Modifier = Modifier
) {
    var start by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        start = true
    }

    BoxWithConstraints(modifier = modifier) {
        val maxW = maxWidth
        val totalPercent = data.values.sum()
        val remainPercent = if (totalPercent < 100) 100 - totalPercent else 0.0

        val entries = data.entries.toList()

        Column {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MyColor.sliver)
                ) {

                    entries.forEachIndexed { index, entry ->
                        val color = entry.key
                        val percent = entry.value

                        val targetWidth = maxW * (percent.toFloat() / 100f)

                        val animatedWidth by animateDpAsState(
                            targetValue = if (start) targetWidth else 0.dp,
                            animationSpec = tween(
                                durationMillis = 900,
                                easing = FastOutSlowInEasing
                            ),
                            label = ""
                        )

                        Box(
                            modifier = Modifier
                                .width(animatedWidth)
                                .height(20.dp)
                                .background(color)
                                .drawBehind {
                                    if (index != entries.lastIndex) {
                                        drawRect(
                                            color = MyColor.white,
                                            topLeft = Offset(size.width - 1.dp.toPx(), 0f),
                                            size = Size(1.dp.toPx(), size.height)
                                        )
                                    }
                                }
                        )
                    }
                    if (remainPercent > 0) {
                        Box(
                            modifier = Modifier
                                .width(maxW * (remainPercent.toFloat() / 100f))
                                .height(20.dp)
                                .background(MyColor.sliver)
                        )
                    }
                }
            }
        }
    }
}