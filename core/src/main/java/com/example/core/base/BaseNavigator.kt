package com.example.core.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.reflect.full.createInstance

object BaseNavigator {
//    @SuppressLint("StaticFieldLeak")
//    private var navController: NavHostController? = null
//    private val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }
//
//    private fun setNavController(nav: NavHostController) { navController = nav }
//
//    fun pushNamed(routeName: String, arguments: Map<String, Any>? = null) {
//        val nav = navController ?: throw IllegalStateException("NavController not initialized")
//        var route = routeName
//        arguments?.let { args ->
//            val queryParams = args.map { "${it.key}=${it.value}" }.joinToString("&")
//            route = "$routeName?$queryParams"
//        }
//        nav.navigate(route)
//    }
//
//    fun pushReplacementNamed(routeName: String, arguments: Map<String, Any>? = null) {
//        val nav = navController ?: throw IllegalStateException("NavController not initialized")
//        var route = routeName
//        arguments?.let { args ->
//            val queryParams = args.map { "${it.key}=${it.value}" }.joinToString("&")
//            route = "$routeName?$queryParams"
//        }
//        nav.navigate(route) {
//            popUpTo(nav.currentDestination?.route ?: "") {
//                inclusive = true
//            }
//        }
//    }
//
//    fun pushNamedAndRemoveUntil(
//        routeName: String,
//        predicate: String? = null,
//        arguments: Map<String, Any>? = null
//    ) {
//        val nav = navController ?: throw IllegalStateException("NavController not initialized")
//        var route = routeName
//        arguments?.let { args ->
//            val queryParams = args.map { "${it.key}=${it.value}" }.joinToString("&")
//            route = "$routeName?$queryParams"
//        }
//        nav.navigate(route) {
//            if (predicate == null) {
//                popUpTo(0)
//            } else {
//                popUpTo(predicate) {
//                    inclusive = false
//                }
//            }
//        }
//    }
//
//    fun popAndPushNamed(routeName: String, arguments: Map<String, Any>? = null) {
//        val nav = navController ?: throw IllegalStateException("NavController not initialized")
//        nav.popBackStack()
//        pushNamed(routeName, arguments)
//    }
//
//    fun pop(result: Map<String, Any>? = null) {
//        val nav = navController ?: throw IllegalStateException("NavController not initialized")
//        nav.popBackStack()
//    }
//
//    fun popUntil(routeName: String) {
//        val nav = navController ?: throw IllegalStateException("NavController not initialized")
//        nav.popBackStack(routeName, inclusive = false)
//    }
//
//    fun canPop(): Boolean {
//        val nav = navController ?: return false
//        return nav.previousBackStackEntry != null
//    }
//
//    fun maybePop(): Boolean {
//        val nav = navController ?: return false
//        return if (nav.previousBackStackEntry != null) {
//            nav.popBackStack()
//            true
//        } else {
//            false
//        }
//    }
//
//    fun pushNamedSingleTop(routeName: String, arguments: Map<String, Any>? = null) {
//        val nav = navController ?: throw IllegalStateException("NavController not initialized")
//        var route = routeName
//        arguments?.let { args ->
//            val queryParams = args.map { "${it.key}=${it.value}" }.joinToString("&")
//            route = "$routeName?$queryParams"
//        }
//        nav.navigate(route) {
//            launchSingleTop = true
//        }
//    }
//
//    fun clearStackAndPushNamed(routeName: String, arguments: Map<String, Any>? = null) {
//        pushNamedAndRemoveUntil(routeName, predicate = null, arguments)
//    }
//
//    @Composable
//    fun BaseNavigation(
//        startPage: String,
//        builder: NavGraphBuilder.() -> Unit
//    ) {
//        val navController = rememberNavController()
//        setNavController(navController) // Inject vào NavigationManager
//        CompositionLocalProvider(LocalNavController provides navController) {
//            NavHost(
//                navController = navController,
//                startDestination = startPage,
//                enterTransition = {
//                    slideInHorizontally(
//                        initialOffsetX = { fullWidth -> fullWidth },
//                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
//                    )
//                },
//                exitTransition = {
//                    slideOutHorizontally(
//                        targetOffsetX = { fullWidth -> -fullWidth / 3 },
//                        animationSpec = tween(300, easing = FastOutSlowInEasing)
//                    ) + fadeOut(
//                        targetAlpha = 0.8f,
//                        animationSpec = tween(300)
//                    )
//                },
//                popEnterTransition = {
//                    slideInHorizontally(
//                        initialOffsetX = { fullWidth -> -fullWidth / 3 },
//                        animationSpec = tween(300, easing = FastOutSlowInEasing)
//                    ) + fadeIn(
//                        initialAlpha = 0.8f,
//                        animationSpec = tween(300)
//                    )
//                },
//                popExitTransition = {
//                    slideOutHorizontally(
//                        targetOffsetX = { fullWidth -> fullWidth },
//                        animationSpec = tween(300, easing = FastOutSlowInEasing)
//                    )
//                }
//            ) { builder() }
//        }
//    }


//    @Composable
//    fun <VM : BaseViewModel, V : BaseView<VM>> config(
//        viewFactory: () -> V,
//        viewModelFactory: @Composable () -> VM
//    ) {
//        val viewModel = viewModelFactory()
//        val view = remember { viewFactory() }
//        LaunchedEffect(viewModel) {
//            view.viewModel = viewModel
//        }
//        view.Render()
//    }

}