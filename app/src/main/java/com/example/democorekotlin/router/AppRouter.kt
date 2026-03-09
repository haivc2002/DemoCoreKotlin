package com.example.democorekotlin.router

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import com.example.core.base.BaseNavigator.ConfigViewNavigator
import com.example.democorekotlin.feature.atin.main.AtinMainView
import com.example.democorekotlin.feature.home.HomeView
import com.example.democorekotlin.feature.login.LoginView

object AppRouter {
    const val LOGIN_VIEW = "LOGIN_VIEW"
    const val HOME_VIEW = "HOME_VIEW"
    const val ATIN_MAIN_VIEW = "ATIN_MAIN_VIEW"

    @Composable
    fun Instance() {
        ConfigViewNavigator(startPage = LOGIN_VIEW) {
            composable(LOGIN_VIEW) { LoginView() }
            composable(HOME_VIEW) { HomeView() }
            composable(ATIN_MAIN_VIEW) { AtinMainView() }
        }
    }
}