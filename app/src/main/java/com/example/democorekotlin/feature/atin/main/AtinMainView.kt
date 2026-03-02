package com.example.democorekotlin.feature.atin.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.base.BaseNavigator.ConfigView
import com.example.core.base.BaseView
import com.example.democorekotlin.feature.home.HomeView
import com.example.democorekotlin.feature.home.HomeViewModel
import com.example.democorekotlin.feature.statistical.StatisticalView
import com.example.democorekotlin.feature.statistical.StatisticalViewModel

class AtinMainView : BaseView<AtinMainViewModel>() {
    companion object {
        const val ROUTER = "AtinMainView"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun BuildRender() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = viewModel.nameView(),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
            },
            bottomBar = {
                BottomNavigationBar()
            },
            floatingActionButton = {
                CheckInFloatingButton()
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (viewModel.pageIndex) {
                    0 -> ConfigView<HomeViewModel, HomeView>()
                    1 -> ConfigView<StatisticalViewModel, StatisticalView>()
                    2 -> PlaceholderView("Lịch sử ra vào")
                    3 -> PlaceholderView("Cá nhân")
                }
            }
        }
    }

    @Composable
    private fun BottomNavigationBar() {
        NavigationBar {
            val items = listOf(
                NavigationItem(
                    label = "Trang chủ",
                    iconSelected = Icons.Filled.Home,
                    iconUnselected = Icons.Outlined.Home,
                    index = 0
                ),
                NavigationItem(
                    label = "Chấm công",
                    iconSelected = Icons.Filled.CalendarMonth,
                    iconUnselected = Icons.Outlined.CalendarMonth,
                    index = 1
                ),
                NavigationItem(
                    label = "Lịch sử",
                    iconSelected = Icons.Filled.Schedule,
                    iconUnselected = Icons.Outlined.Schedule,
                    index = 2
                ),
                NavigationItem(
                    label = "Cá nhân",
                    iconSelected = Icons.Filled.AccountCircle,
                    iconUnselected = Icons.Outlined.AccountCircle,
                    index = 3
                )
            )

            items.forEach { item ->
                NavigationBarItem(
                    selected = viewModel.pageIndex == item.index,
                    onClick = { viewModel.onChangeView(item.index) },
                    icon = {
                        Icon(
                            imageVector = if (viewModel.pageIndex == item.index)
                                item.iconSelected
                            else
                                item.iconUnselected,
                            contentDescription = item.label
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = if (viewModel.pageIndex == item.index)
                                FontWeight.Bold
                            else
                                FontWeight.Normal
                        )
                    }
                )
            }
        }
    }

    @Composable
    private fun CheckInFloatingButton() {
        FloatingActionButton(
            onClick = { viewModel.onOpenCheckIn() },
            shape = CircleShape,
            containerColor = Color.Transparent,
            modifier = Modifier.size(70.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF9B7EFF),
                                Color(0xFF6E5BFF)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Face Check-in",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }

    @Composable
    private fun PlaceholderView(title: String) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                color = Color.Gray
            )
        }
    }
}

private data class NavigationItem(
    val label: String,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
    val index: Int
)