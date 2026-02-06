package com.example.democorekotlin

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.core.base.BaseNavigator
import com.example.democorekotlin.feature.home.HomeView
import com.example.democorekotlin.feature.home.HomeViewModel
import com.example.democorekotlin.feature.login.LoginView
import com.example.democorekotlin.model.ModelLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AppNavHost()
            }
        }
    }

    @Composable
    fun AppNavHost(
        navController: NavHostController = rememberNavController()
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeView.ROUTER
        ) {
            composable(HomeView.ROUTER) {
//                BaseNavigator.config(
//                    viewFactory = { HomeView() },
//                    viewModelFactory = { hiltViewModel<HomeViewModel>() }
//                )
                LoginView().BuildRender()

            }
        }
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val data: ModelLogin) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

