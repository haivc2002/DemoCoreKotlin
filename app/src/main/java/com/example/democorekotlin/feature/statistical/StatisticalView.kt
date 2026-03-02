package com.example.democorekotlin.feature.statistical

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.base.BaseView
import com.example.core.common.OnlyMessageWidget
import com.example.core.widget.WidgetListView
import com.example.core.widget.WidgetState
import com.example.democorekotlin.model.response.TimekeepingData
import com.example.democorekotlin.res.MyColor
import com.example.democorekotlin.utils.Utils
import com.example.democorekotlin.widget.AtinBox
import com.example.democorekotlin.widget.AtinDonutChart
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import java.time.DayOfWeek
import java.time.LocalDate

class StatisticalView : BaseView<StatisticalViewModel>() {
    companion object {
        const val ROUTER: String = "StatisticalView"
    }

    @Composable
    override fun BuildRender() {
        WidgetState(
            state = viewModel.timeKeepingState,
            onSuccess = {
                WidgetListView {
                    item { CalendarItem(it.data ?: emptyList()) }
                    item { ItemStatistical() }
                }
            },
            onError = {
                OnlyMessageWidget(it)
            }
        )
    }

    @Composable
    fun CalendarItem(data: List<TimekeepingData>) {
        val state = rememberCalendarState(
            startMonth = viewModel.currentMonth.minusMonths(12),
            endMonth = viewModel.currentMonth.plusMonths(12),
            firstVisibleMonth = viewModel.currentMonth,
            firstDayOfWeek = DayOfWeek.MONDAY,
            outDateStyle = OutDateStyle.EndOfGrid
        )

        val scope = rememberCoroutineScope()
        val visibleMonth = state.firstVisibleMonth.yearMonth
        val daysOfWeek = DayOfWeek.entries.toTypedArray()

        AtinBox(
            modifier = Modifier.fillMaxSize(),
            colors = listOf(MyColor.white)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton({
                        viewModel.onChangeMoth(scope, state, "PREVIOUS")
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = "${visibleMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${visibleMonth.year}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton({
                        viewModel.onChangeMoth(scope, state, "NEXT")
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Back"
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysOfWeek.forEach { dayOfWeek ->
                        Text(
                            modifier = Modifier.weight(1f),
                            text = dayOfWeek.name.take(3),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                HorizontalCalendar(
                    state = state,
                    userScrollEnabled = false,
                    dayContent = { day ->
                        val isOutsideMonth = day.position != DayPosition.MonthDate
                        val isToday = day.date == LocalDate.now()
                        if (isOutsideMonth) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                                    .aspectRatio(1f)
                            )
                            return@HorizontalCalendar
                        }
                        val status : Utils.ItemStatus = viewModel.getItemStatus(day.date, data)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(status.color)
                                .clickable { /* handle click */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.date.dayOfMonth.toString(),
                                color = if(isToday) MyColor.lavenderBlue else MyColor.white,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )

            }
        }
    }

    @Composable
    fun ItemStatistical() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AtinBox(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center,
                colors = listOf(MyColor.white)
            ) {
                AtinDonutChart(
                    data = mapOf(
                        MyColor.lavenderBlue to 40.0,
                        MyColor.orange to 30.0,
                        MyColor.red to 20.0,
                    ),
                    modifier = Modifier.padding(vertical = 28.dp),
                    strokeWidth = 40.dp,
                    size = 130.dp,
                )
            }
        }
    }
}