package com.example.democorekotlin.feature.statistical

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.example.core.api.ApiResult
import com.example.core.base.BaseViewModel
import com.example.core.widget.StateApi
import com.example.democorekotlin.model.response.ModelTimekeeping
import com.example.democorekotlin.model.response.TimekeepingData
import com.example.democorekotlin.network.Repository
import com.example.democorekotlin.res.MyColor
import com.example.democorekotlin.utils.Utils
import com.example.democorekotlin.utils.fDatetime
import com.kizitonwose.calendar.compose.CalendarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class StatisticalViewModel @Inject constructor(
    private val repository: Repository,
) : BaseViewModel() {

    var currentMonth: YearMonth by mutableStateOf(YearMonth.now())
        private set
    var timeKeepingState by mutableStateOf<StateApi<ModelTimekeeping>>(StateApi.Loading)

    init {
        onGetTimekeeping(currentMonth.year, currentMonth.month.value)
    }

    fun onGetTimekeeping(year: Int, month: Int) {
        val yearMonth = YearMonth.of(year, month)
        viewModelScope.launch {
            val fromDate: String = yearMonth
                .atDay(1)
                .atStartOfDay()
                .fDatetime("yyyy-MM-dd")
            val toDate: String = yearMonth
                .atEndOfMonth()
                .atTime(LocalTime.MAX)
                .fDatetime("yyyy-MM-dd")
            val response = repository.timeKeepingHistoryApi(fromDate, toDate)
            timeKeepingState = when (response) {
                is ApiResult.Success -> {
                    val apiList = response.value.data.orEmpty()
                    if (apiList.isEmpty()) {
                        StateApi.Success(response.value.copy(data = emptyList()))
                        return@launch
                    }
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val dataByDate = buildMap {
                        apiList.forEach { item ->
                            val date = item.day
                                ?.runCatching { LocalDate.parse(this, formatter) }
                                ?.getOrNull()
                                ?: return@forEach
                            putIfAbsent(date, item)
                        }
                    }
                    if (dataByDate.isEmpty()) {
                        StateApi.Success(response.value.copy(data = emptyList()))
                        return@launch
                    }
                    val maxDate = dataByDate.keys.maxOrNull()!!
                    val isFull = dataByDate.size == maxDate.dayOfMonth &&
                            dataByDate.keys.all { it.dayOfMonth <= maxDate.dayOfMonth }
                    val finalMap = if (isFull) {
                        dataByDate
                    } else {
                        buildMap {
                            putAll(dataByDate)
                            (1 until maxDate.dayOfMonth).forEach { day ->
                                val date = LocalDate.of(maxDate.year, maxDate.month, day)
                                val fakeData = TimekeepingData(day = date.format(formatter))
                                putIfAbsent(date, fakeData)
                            }
                        }
                    }
                    val finalList = finalMap.toSortedMap().values.toList()
                    StateApi.Success(response.value.copy(data = finalList))
                }
                is ApiResult.Failure -> StateApi.Failure(response.errorCode)
            }
        }
    }

    fun onChangeMoth(
        scope: CoroutineScope,
        state: CalendarState,
        navigator: String
    ) {
        val visibleMonth = state.firstVisibleMonth.yearMonth

        val targetMonth = if (navigator == "NEXT") {
            if (isCurrentOrFutureMonth(visibleMonth)) return
            visibleMonth.plusMonths(1)
        } else {
            visibleMonth.minusMonths(1)
        }
        currentMonth = targetMonth
        scope.launch { state.animateScrollToMonth(targetMonth) }
        onGetTimekeeping(targetMonth.year, targetMonth.monthValue)
    }

    fun isCurrentOrFutureMonth(month: YearMonth): Boolean {
        val now = YearMonth.now()
        return month >= now
    }

    fun getItemStatus(day: LocalDate, listTkp: List<TimekeepingData>): Utils.ItemStatus {
        if (YearMonth.from(day) != currentMonth) {
            return Utils.ItemStatus("", MyColor.transparent)
        }
        val dayIndex = day.dayOfMonth - 1
        val isFutureDate = dayIndex >= listTkp.size
        print(listTkp)

        if (isFutureDate) {
            return Utils.itemStatus(
                isLeaveWork = false,
                isWeekend = false,
                isFutureDate = true,
                isRealTkp = false,
                isLateTkp = false,
                isEarlyLeaveTkp = false,
            )
        }

        val data = listTkp[dayIndex]
        val latest = data.latestCheckInMinute
        val earliest = data.earliestCheckInMinute
        val isNotCheckIn = latest == null
        val isNotCheckOut = earliest == null
        val isWeekend = day.dayOfWeek == DayOfWeek.SATURDAY ||
                day.dayOfWeek == DayOfWeek.SUNDAY
        val isLeaveWork = isNotCheckIn && isNotCheckOut && !isWeekend
        val latestVal = latest ?: -1
        val earliestVal = earliest ?: -1
        val isRealTkp =
            (latestVal == 0 && earliestVal == 0) ||
                    (latestVal == 0 && isNotCheckOut)
        val isLateTkp =
            ((latestVal > 0) && earliestVal == 0) ||
                    (isNotCheckIn && earliestVal == 0) ||
                    ((latestVal > 0) && (earliestVal > 0))
        val isEarlyLeaveTkp = (!isNotCheckOut && earliestVal > 0)
        return Utils.itemStatus(
            isLeaveWork = isLeaveWork,
            isWeekend = isWeekend,
            isFutureDate = false,
            isRealTkp = isRealTkp,
            isLateTkp = isLateTkp,
            isEarlyLeaveTkp = isEarlyLeaveTkp,
        )
    }

    fun countStatus(listTkp: List<TimekeepingData>): Map<Color, Int> {
        val counter = mutableMapOf(
            MyColor.lavenderBlue to 0,
            MyColor.orange to 0,
            MyColor.red to 0,
            MyColor.lavenderPink to 0
        )
        val totalDays = currentMonth.lengthOfMonth()
        for (i in 1..totalDays) {
            val day = currentMonth.atDay(i)
            val status = getItemStatus(day,listTkp)
            val color = status.color
            counter[color] = (counter[color] ?: 0) + 1
        }
        return counter
    }
}