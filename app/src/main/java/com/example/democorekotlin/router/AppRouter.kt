package com.example.democorekotlin.router

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import com.example.core.base.BaseNavigator.ConfigView
import com.example.core.base.BaseNavigator.ConfigViewNavigator
import com.example.democorekotlin.feature.atin.main.AtinMainView
import com.example.democorekotlin.feature.atin.main.AtinMainViewModel
import com.example.democorekotlin.feature.home.HomeView
import com.example.democorekotlin.feature.home.HomeViewModel
import com.example.democorekotlin.feature.login.LoginView
import com.example.democorekotlin.feature.login.LoginViewModel
import com.example.democorekotlin.feature.statistical.StatisticalView
import com.example.democorekotlin.feature.statistical.StatisticalViewModel

object AppRouter {
    @Composable
    fun Instance() {
        ConfigViewNavigator(startPage = LoginView.ROUTER) {
            composable(LoginView.ROUTER) {
                ConfigView<LoginViewModel, LoginView>()
            }
            composable(HomeView.ROUTER) {
                ConfigView<HomeViewModel, HomeView>()
            }
            composable(AtinMainView.ROUTER) {
                ConfigView<AtinMainViewModel, AtinMainView>()
            }
            composable(StatisticalView.ROUTER) {
                ConfigView<StatisticalViewModel, StatisticalView>()
            }
        }
    }
}