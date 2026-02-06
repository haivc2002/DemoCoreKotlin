package com.example.democorekotlin.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.base.BaseView

class HomeView : BaseView<HomeViewModel>() {
    companion object {
        const val ROUTER: String = "HomeView"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun buildRender() {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Home") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hello Home Screen!",
                    fontSize = 24.sp
                )
                Text(
                    text = "Welcome to your application.",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}