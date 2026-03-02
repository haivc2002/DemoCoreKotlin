package com.example.democorekotlin.feature.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.core.base.BaseView
import com.example.core.common.OnlyMessageWidget
import com.example.core.widget.WidgetListView
import com.example.core.widget.WidgetState
import com.example.democorekotlin.res.MyColor
import com.example.democorekotlin.utils.Utils
import com.example.democorekotlin.utils.fDatetime
import com.example.democorekotlin.utils.weekdayName
import com.example.democorekotlin.widget.AtinBox
import com.example.democorekotlin.widget.AtinLineChart

class HomeView : BaseView<HomeViewModel>() {
    companion object {
        const val ROUTER: String = "HomeView"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun BuildRender() {
        val context = LocalContext.current
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            viewModel.onLocationPermissionResult(granted)
        }

        LaunchedEffect(Unit) {
            val hasFine = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val hasCoarse = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasFine || hasCoarse) {
                viewModel.onLocationPermissionResult(true)
            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    )
                )
            }
        }

        WidgetListView(
            onRefresh = { }
        ) {
            item {
                AtinBox(
                    modifier = Modifier.fillMaxWidth(),
                    padding = PaddingValues(0.dp),
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .weight(1f)
                            ) {
                                WidgetState(
                                    state = viewModel.userInfoState,
                                    onError = { Text("Lỗi lấy thông tin") },
                                    onSuccess = { Text(it.data?.fullname ?: "Không có thông tin",
                                        fontWeight = FontWeight.Bold
                                    ) },
                                    onLoading = { Text("Đang lấy thông tin") }
                                )
                                Text(viewModel.dateTime.weekdayName() +
                                        " ngày ${viewModel.dateTime.fDatetime()}"
                                )
                                WidgetState(
                                    state = viewModel.weatherState,
                                    onLoading = { Text("__") },
                                    onSuccess = {
                                        Text("Chào ${viewModel.getTimeOfDay(
                                            it.dt ?: 0,
                                            it.sys?.sunrise ?: 0,
                                            it.sys?.sunset ?: 0,
                                        )}")
                                    },
                                    onError = { Text("Lỗi lấy thông tin") },
                                )
                            }
                            Text(viewModel.dateTime.fDatetime("HH:mm:ss"))
                        }
                        AtinBox(
                            modifier = Modifier.fillMaxWidth(),
                            colors = listOf(MyColor.white.copy(0.2f), MyColor.white.copy(0.0f)),
                            radius = 0.dp
                        ) {
                            WidgetState(
                                state = viewModel.weatherState,
                                onError = {
                                    OnlyMessageWidget(it, Modifier.fillMaxWidth())
                                },
                                onLoading = { Text("__") },
                                onSuccess = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = viewModel.iconWeather(it),
                                            contentDescription = "icon",
                                            modifier = Modifier.size(40.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                        Text(
                                            text = viewModel.tempValue(it),
                                            style = TextStyle(
                                                color = MyColor.white,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp,
                                                shadow = Shadow(
                                                    color = Color.Black,
                                                    blurRadius = 25f
                                                )
                                            ),
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                        Text(
                                            text = " | Độ ẩm: ${it.main?.humidity ?: ""}%",
                                            style = TextStyle(
                                                color = MyColor.white,
                                                shadow = Shadow(
                                                    color = Color.Black,
                                                    blurRadius = 25f
                                                )
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 8.dp)
                                        )

                                    }
                                }
                            )
                        }
                    }
                }
            }
            item {
                AtinBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = listOf(MyColor.white),
                    padding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconCircle(
                            color = MyColor.red,
                            icon = Icons.Default.LocationOn,
                        )
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(1f)
                        ) {
                            Text("Vị trí hiện tại", fontWeight = FontWeight.Bold)
                            WidgetState(
                                state = viewModel.currentLocationState,
                                onError = { OnlyMessageWidget(it, modifier = Modifier
                                    .fillMaxWidth()) },
                                onSuccess = { Text("${it.locality}, " +
                                    "${it.city}, " +
                                    "${it.countryName}")},
                                onLoading = { Text("Đang lấy vị trí") }
                            )
                        }
                    }
                }
            }
            item {
                AtinBox(
                    modifier = Modifier.fillMaxWidth(),
                    colors = listOf(MyColor.white),
                    padding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    )
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconCircle(
                                color = MyColor.lavenderBlue,
                                icon = Icons.Default.BookmarkAdded,
                                size = 20.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Chấm công hôm nay",
                                modifier = Modifier.weight(1f),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            TextButton(
                                onClick = { /* TODO */ },
                                contentPadding = PaddingValues(
                                    vertical = 2.dp,
                                    horizontal = 20.dp
                                )
                            ) {
                                Text("Xem thêm")
                            }
                        }
                        WidgetState(
                            state = viewModel.checkInHistoryState,
                            onError = {
                                OnlyMessageWidget(it, modifier = Modifier
                                    .fillMaxWidth())
                            },
                            onSuccess = {
                                val list = it.data ?: emptyList()
                                val maxItem = minOf(list.size, 3)

                                if (list.isEmpty()) {
                                    OnlyMessageWidget(
                                        "Bạn chưa chấm công ngày hôm nay",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    Column {
                                        repeat(maxItem) { index ->

                                            val ghostWhite = MaterialTheme.colorScheme.background
                                            val white = MaterialTheme.colorScheme.surface

                                            val shape = RoundedCornerShape(
                                                topStart = if (index == 0) 13.dp else 0.dp,
                                                topEnd = if (index == 0) 13.dp else 0.dp,
                                                bottomStart = if (index == maxItem - 1) 13.dp else 0.dp,
                                                bottomEnd = if (index == maxItem - 1) 13.dp else 0.dp
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(shape)
                                                    .background(if (index % 2 == 0) ghostWhite else white)
                                                    .border(1.dp, ghostWhite, shape)
                                                    .padding(horizontal = 15.dp, vertical = 7.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = MyColor.green
                                                    )
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Row(
                                                        modifier = Modifier.weight(2f)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.LocationOn,
                                                            contentDescription = null,
                                                            tint = MyColor.lavenderPink
                                                        )
                                                        Spacer(Modifier.width(10.dp))
                                                        Text(list[index].locationName ?: "Không có thông tin")
                                                    }
                                                    Row(
                                                        modifier = Modifier.weight(1f)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.AccessTimeFilled,
                                                            contentDescription = null,
                                                            tint = MyColor.lavenderPink
                                                        )
                                                        Spacer(Modifier.width(10.dp))
                                                        Text(list[index].accessTime ?: "Không có thông tin")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        )
                    }
                }
            }
            item { ChartStatistical() }
        }
    }

    @Composable
    fun ChartStatistical() {
        val countDayLavenderBlue : Int = viewModel.countStatus()[MyColor.lavenderBlue] ?: 0
        val countDayOrange : Int = viewModel.countStatus()[MyColor.orange] ?: 0
        val countDayLavenderPink : Int = viewModel.countStatus()[MyColor.lavenderPink] ?: 0
        val countDayRed : Int = viewModel.countStatus()[MyColor.red] ?: 0

        val totalDayInMonth = Utils.daysInMonth(viewModel.dateTime)

        val percentLavenderBlue = (countDayLavenderBlue.toDouble() / totalDayInMonth) * 100
        val percentOrange = (countDayOrange.toDouble() / totalDayInMonth) * 100
        val percentLavenderPink = (countDayLavenderPink.toDouble() / totalDayInMonth) * 100
        val percentRed = (countDayRed.toDouble() / totalDayInMonth) * 100

        AtinBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            colors = listOf(MyColor.white),
            padding = PaddingValues(
                horizontal = 20.dp,
                vertical = 10.dp
            )
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconCircle(
                        color = MyColor.lavenderPink,
                        icon = Icons.Default.Analytics,
                        size = 20.dp
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .weight(1f)
                    ) {
                        Text("Thống kê tháng này", style = TextStyle(fontWeight = FontWeight.Bold))
                        Text("Tháng ${viewModel.dateTime.fDatetime("MM, yyyy")}")
                    }
                    TextButton(
                        onClick = { /* TODO */ },
                        contentPadding = PaddingValues(
                            vertical = 2.dp,
                            horizontal = 20.dp
                        )
                    ) {
                        Text("Xem thêm")
                    }
                }
                AtinLineChart(
                    data = mapOf(
                        MyColor.lavenderBlue to percentLavenderBlue,
                        MyColor.orange to percentOrange,
                        MyColor.lavenderPink to percentLavenderPink,
                        MyColor.red to percentRed,
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                ) {
                    BoxStatistical(
                        title = "Đúng giờ",
                        value = countDayLavenderBlue.toString(),
                        color = MyColor.lavenderBlue,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp)
                    )
                    BoxStatistical(
                        title = "Muộn giờ",
                        value = countDayOrange.toString(),
                        color = MyColor.orange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                ) {
                    BoxStatistical(
                        title = "Về sớm",
                        value = countDayLavenderPink.toString(),
                        color = MyColor.lavenderPink,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp)
                    )
                    BoxStatistical(
                        title = "Nghỉ làm",
                        value = countDayRed.toString(),
                        color = MyColor.red,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp)
                    )
                }

            }
        }
    }

    @Composable
    fun IconCircle(
        color: Color,
        icon: ImageVector,
        size: Dp = 24.dp
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color.copy(alpha = 0.3f))
                .padding(size / 3),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(size),
                tint = color
            )
        }
    }

    @Composable
    fun BoxStatistical(
        title: String,
        value: String,
        color: Color,
        modifier: Modifier = Modifier
    ) {
        AtinBox(
            modifier = modifier
                .fillMaxSize()
                .aspectRatio(2f),
            radius = 15.dp,
            colors = listOf(color, color.copy(alpha = 0.5f))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = MyColor.white,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = value,
                    style = TextStyle(
                        color = MyColor.white,
                        fontWeight = FontWeight.Bold,
                        fontSize = 35.sp
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}