package com.example.democorekotlin.feature.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.example.core.api.ApiResult
import com.example.core.base.BaseViewModel
import com.example.core.widget.StateApi
import com.example.democorekotlin.model.response.*
import com.example.democorekotlin.network.Repository
import com.example.democorekotlin.res.MyColor
import com.example.democorekotlin.utils.Utils
import com.example.democorekotlin.utils.fDatetime
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    var checkInHistoryState by mutableStateOf<StateApi<ModelListCheckIn>>(StateApi.Loading)
    var dateTime by mutableStateOf<LocalDateTime>(LocalDateTime.now())
    var userInfoState by mutableStateOf<StateApi<ModelInfo>>(StateApi.Loading)
    var weatherState by mutableStateOf<StateApi<ModelWeather>>(StateApi.Loading)
    var currentLocationState by mutableStateOf<StateApi<ModelCurrentLocation>>(StateApi.Loading)
    var timeKeepingState by mutableStateOf<StateApi<ModelTimekeeping>>(StateApi.Loading)

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    init {
        if(userInfoState !is StateApi.Success)
            onGetUserInfo()
        if(checkInHistoryState !is StateApi.Success)
            onGetListCheckIn()
        if(timeKeepingState !is StateApi.Success)
            onGetListTimeKeeping()
        startClock()
    }

    private fun onGetListCheckIn() {
        val today = LocalDate.now()
        val fromTime: LocalDateTime = today.atStartOfDay()
        val toTime: LocalDateTime = today.atTime(23, 59, 59)
        viewModelScope.launch {
            checkInHistoryState = StateApi.Loading
            val response = repository.checkInHistoryApi(fromTime.toString(), toTime.toString())
            checkInHistoryState = when (response) {
                is ApiResult.Success -> StateApi
                    .Success(response.value)
                is ApiResult.Failure -> StateApi
                    .Failure(response.errorCode)
            }
        }
    }

    private fun onGetUserInfo() {
        viewModelScope.launch {
            userInfoState = StateApi.Loading
            val response = repository.userInfoApi()
            userInfoState = when (response) {
                is ApiResult.Success -> StateApi
                    .Success(response.value)
                is ApiResult.Failure -> StateApi
                    .Failure(response.errorCode)
            }
        }
    }

    private fun onGetWeather(lat: String, lon: String) {
        viewModelScope.launch {
//            weatherState = StateApi.Loading
            val response = repository.weatherApi(lat, lon)
            weatherState = when (response) {
                is ApiResult.Success -> StateApi
                    .Success(response.value)
                is ApiResult.Failure -> StateApi
                    .Failure(response.errorCode)
            }
        }
    }

    private fun onGetCurrentLocation(lat: String, lon: String) {
        viewModelScope.launch {
            val response = repository.currentLocationApi(lat, lon)
            currentLocationState = when (response) {
                is ApiResult.Success -> StateApi
                    .Success(response.value)
                is ApiResult.Failure -> StateApi
                    .Failure(response.errorCode)
            }
        }
    }

    private fun onGetListTimeKeeping() {
        val today = LocalDate.now()
        val fromDate = today.withDayOfMonth(1).atStartOfDay()
        val toDate = today
            .withDayOfMonth(today.lengthOfMonth())
            .atTime(23, 59, 59)
        viewModelScope.launch {
            timeKeepingState = StateApi.Loading
            val response = repository.timeKeepingHistoryApi(
                fromDate.fDatetime("yyyy-MM-dd"),
                toDate.fDatetime("yyyy-MM-dd"),
            )
            timeKeepingState = when (response) {
                is ApiResult.Success -> {
                    val apiList = response.value.data ?: emptyList()
                    if (apiList.isEmpty()) {
                        val data = response.value.copy(data = emptyList())
                        StateApi.Success(data)
                        return@launch
                    }
                    val dataByDate = mutableMapOf<LocalDate, TimekeepingData>()
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    for (item in apiList) {
                        val dayStr = item.day ?: continue
                        val date = try {
                            LocalDate.parse(dayStr, formatter)
                        } catch (_: Exception) {
                            null
                        } ?: continue
                        dataByDate.putIfAbsent(date, item)
                    }
                    if (dataByDate.isEmpty()) {
                        val data = response.value.copy(data = emptyList())
                        StateApi.Success(data)
                        return@launch
                    }
                    val maxDate = dataByDate.keys.maxOrNull()!!
                    val isFull = dataByDate.size == maxDate.dayOfMonth &&
                            dataByDate.keys.all { it.dayOfMonth in 1..maxDate.dayOfMonth }
                    if (!isFull) {
                        for (day in 1 until maxDate.dayOfMonth) {
                            val date = LocalDate.of(maxDate.year, maxDate.month, day)
                            val fakeData = TimekeepingData(day = date.format(formatter))
                            dataByDate.putIfAbsent(date, fakeData)
                        }
                    }
                    val result = dataByDate.toList().sortedBy { it.first }
                    val uniqueList = result.map { it.second }
                    val data = response.value.copy(data = uniqueList)
                    StateApi.Success(data)
                }
                is ApiResult.Failure -> StateApi
                    .Failure(response.errorCode)
            }
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {
        if (!granted) {
            val message = "Vui lòng cấp quyền vị trí trong Cài đặt"
            weatherState = StateApi.Failure(message)
            currentLocationState = StateApi.Failure(message)
            return
        }
        fetchLocation()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @Suppress("MissingPermission")
    private fun fetchLocation() {
        if (!hasLocationPermission()) {
            val message = "Vui lòng cấp quyền vị trí trong Cài đặt"
            weatherState = StateApi.Failure(message)
            currentLocationState = StateApi.Failure(message)
            return
        }
        viewModelScope.launch {
            try {
                val cancellationToken = CancellationTokenSource()
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationToken.token
                ).await()
                if (location != null) {
                    val latitude = location.latitude.toString()
                    val longitude = location.longitude.toString()
                    onGetCurrentLocation(latitude, longitude)
                    onGetWeather(latitude, longitude)
                } else {
                    val message = "Không thể lấy vị trí, vui lòng bật GPS"
                    weatherState = StateApi.Failure(message)
                    currentLocationState = StateApi.Failure(message)
                }
            } catch (_: Exception) {
                val message = "Không thể lấy vị trí, vui lòng bật GPS"
                weatherState = StateApi.Failure(message)
                currentLocationState = StateApi.Failure(message)
            }
        }
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                dateTime = LocalDateTime.now()
                delay(1000)
            }
        }
    }

    private fun getItemStatus(day: LocalDate): Utils.ItemStatus {
        val currentMonth = dateTime.toLocalDate()
        if (day.year != currentMonth.year || day.monthValue != currentMonth.monthValue) {
            return Utils.ItemStatus("", MyColor.transparent)
        }
        val listTkp = (timeKeepingState as? StateApi.Success)?.data?.data
            ?: return Utils.ItemStatus("", MyColor.transparent)
        val dayIndex = day.dayOfMonth - 1
        val isFutureDate = dayIndex >= listTkp.size
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
        val isEarlyLeaveTkp =
            earliestVal > 0

        return Utils.itemStatus(
            isLeaveWork = isLeaveWork,
            isWeekend = isWeekend,
            isFutureDate = false,
            isRealTkp = isRealTkp,
            isLateTkp = isLateTkp,
            isEarlyLeaveTkp = isEarlyLeaveTkp,
        )
    }

    fun countStatus(): Map<Color, Int> {
        val counter = mutableMapOf(
            MyColor.lavenderBlue to 0,
            MyColor.orange to 0,
            MyColor.red to 0,
            MyColor.lavenderPink to 0
        )
        val date = dateTime.toLocalDate()
        val year = date.year
        val month = date.monthValue
        val totalDays = date.lengthOfMonth()
        for (i in 1..totalDays) {
            val day = LocalDate.of(year, month, i)
            val status = getItemStatus(day)
            val color = status.color
            counter[color] = (counter[color] ?: 0) + 1
        }
        return counter
    }

    fun getTimeOfDay(dt: Int, sunrise: Int, sunset: Int): String {
        val zone = ZoneId.systemDefault()
        val now = Instant.ofEpochSecond(dt.toLong()).atZone(zone).toLocalDateTime()
        val sunriseTime = Instant.ofEpochSecond(sunrise.toLong()).atZone(zone).toLocalDateTime()
        val sunsetTime = Instant.ofEpochSecond(sunset.toLong()).atZone(zone).toLocalDateTime()
        if (now.isBefore(sunriseTime)) return "buổi tối"
        if (now.isAfter(sunsetTime)) return "buổi tối"
        val hour = now.hour
        return when {
            hour < 11 -> "buổi sáng"
            hour < 13 -> "buổi trưa"
            hour < 18 -> "buổi chiều"
            else -> "buổi tối"
        }
    }

    fun iconWeather(model: ModelWeather): String {
        val listWeather = model.weather ?: emptyList()
        if (listWeather.isEmpty()) return ""
        return "https://openweathermap.org/img/wn/${listWeather[0].icon}@2x.png"
    }

    fun tempValue(model: ModelWeather): String {
        val value = model.main?.temp ?: 0.0
        return "${(value - 273.15).roundToInt()}ºC"
    }
}