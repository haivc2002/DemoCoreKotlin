package com.example.core.base

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlin.reflect.full.createInstance

/**
 * BaseNavigator - Global navigation manager for Compose applications
 * 
 * This singleton object provides navigation methods between screens in the application.
 * It wraps Jetpack Compose's NavController and provides a more user-friendly API.
 * 
 * @property navController NavController initialized by [ConfigViewNavigator]
 */
object BaseNavigator {
    /**
     * Internal NavController to manage the navigation stack
     * Automatically initialized when calling [ConfigViewNavigator]
     */
    @SuppressLint("StaticFieldLeak")
    private var navController: NavHostController ?= null

    /**
     * Navigate to a new screen and add it to the navigation stack
     * 
     * @param routeName Route name of the destination screen (typically View.ROUTER)
     * @param arguments Map of parameters passed via URL query string (optional)
     * @throws IllegalStateException if NavController is not initialized
     * 
     * Example:
     * ```kotlin
     * // Simple navigation
     * pushNamed(DetailView.ROUTER)
     * 
     * // Navigation with arguments
     * pushNamed(
     *     DetailView.ROUTER,
     *     mapOf("id" to 123, "title" to "Hello")
     * )
     * ```
     */
    @JvmStatic
    fun pushNamed(routeName: String, arguments: Map<String, Any>? = null) {
        val nav = navController ?: throw IllegalStateException("NavController not initialized")
        var route = routeName
        arguments?.let { args ->
            val queryParams = args.map { "${it.key}=${it.value}" }.joinToString("&")
            route = "$routeName?$queryParams"
        }
        nav.navigate(route)
    }

    /**
     * Replace the current screen with a new screen
     * The current screen will be removed from the stack
     * 
     * @param routeName Route name of the destination screen
     * @param arguments Map of parameters passed via URL query string (optional)
     * @throws IllegalStateException if NavController is not initialized
     * 
     * Example:
     * ```kotlin
     * // Replace login screen with home screen after successful login
     * pushReplacementNamed(HomeView.ROUTER)
     * ```
     */
    @JvmStatic
    fun pushReplacementNamed(routeName: String, arguments: Map<String, Any>? = null) {
        val nav = navController ?: throw IllegalStateException("NavController not initialized")
        var route = routeName
        arguments?.let { args ->
            val queryParams = args.map { "${it.key}=${it.value}" }.joinToString("&")
            route = "$routeName?$queryParams"
        }
        nav.navigate(route) {
            popUpTo(nav.currentDestination?.route ?: "") {
                inclusive = true
            }
        }
    }

    /**
     * Navigate to a new screen and remove all screens up to a specific point
     * 
     * @param routeName Route name of the destination screen
     * @param predicate Route to keep in the stack. If null, clears the entire stack
     * @param arguments Map of parameters passed via URL query string (optional)
     * @throws IllegalStateException if NavController is not initialized
     * 
     * Example:
     * ```kotlin
     * // Clear entire stack and go to home screen (logout scenario)
     * pushNamedAndRemoveUntil(HomeView.ROUTER, predicate = null)
     * 
     * // Go back to home screen and remove all intermediate screens
     * pushNamedAndRemoveUntil(
     *     SuccessView.ROUTER,
     *     predicate = HomeView.ROUTER
     * )
     * ```
     */
    @JvmStatic
    fun pushNamedAndRemoveUntil(
        routeName: String,
        predicate: String? = null,
        arguments: Map<String, Any>? = null
    ) {
        val nav = navController ?: throw IllegalStateException("NavController not initialized")
        var route = routeName
        arguments?.let { args ->
            val queryParams = args.map { "${it.key}=${it.value}" }.joinToString("&")
            route = "$routeName?$queryParams"
        }
        nav.navigate(route) {
            if (predicate == null) {
                popUpTo(0)
            } else {
                popUpTo(predicate) {
                    inclusive = false
                }
            }
        }
    }

    /**
     * Pop the previous screen, then navigate to a new screen
     * 
     * @param routeName Route name of the destination screen
     * @param arguments Map of parameters passed via URL query string (optional)
     * @throws IllegalStateException if NavController is not initialized
     * 
     * Example:
     * ```kotlin
     * // Go back and navigate to a different screen
     * popAndPushNamed(AlternativeView.ROUTER)
     * ```
     */
    @JvmStatic
    fun popAndPushNamed(routeName: String, arguments: Map<String, Any>? = null) {
        val nav = navController ?: throw IllegalStateException("NavController not initialized")
        nav.popBackStack()
        pushNamed(routeName, arguments)
    }

    /**
     * Go back to the previous screen in the navigation stack
     * 
     * @param result Map of results to return to the previous screen (not yet implemented)
     * @throws IllegalStateException if NavController is not initialized
     * 
     * Example:
     * ```kotlin
     * // Simple pop
     * pop()
     * 
     * // Pop with result
     * pop(mapOf("selected" to item))
     * ```
     */
    @JvmStatic
    fun pop(result: Map<String, Any>? = null) {
        val nav = navController ?: throw IllegalStateException("NavController not initialized")
        nav.popBackStack()
    }

    /**
     * Pop back to a specific screen in the navigation stack
     * 
     * @param routeName Route name of the destination screen to pop back to
     * @throws IllegalStateException if NavController is not initialized
     * 
     * Example:
     * ```kotlin
     * // Pop back to home screen, skipping all intermediate screens
     * popUntil(HomeView.ROUTER)
     * ```
     */
    @JvmStatic
    fun popUntil(routeName: String) {
        val nav = navController ?: throw IllegalStateException("NavController not initialized")
        nav.popBackStack(routeName, inclusive = false)
    }

    /**
     * Check if it's possible to pop back to the previous screen
     * 
     * @return true if there's a previous screen in the stack, false otherwise
     * 
     * Example:
     * ```kotlin
     * if (canPop()) {
     *     // Can go back
     *     pop()
     * } else {
     *     // This is the first screen, handle app exit
     *     activity.finish()
     * }
     * ```
     */
    @JvmStatic
    fun canPop(): Boolean {
        val nav = navController ?: return false
        return nav.previousBackStackEntry != null
    }

    /**
     * Attempt to pop back to the previous screen if possible
     * 
     * @return true if successfully popped, false if unable to pop
     * 
     * Example:
     * ```kotlin
     * // Handle back button press
     * if (!maybePop()) {
     *     // Cannot go back, exit the app
     *     activity.finish()
     * }
     * ```
     */
    @JvmStatic
    fun maybePop(): Boolean {
        val nav = navController ?: return false
        return if (nav.previousBackStackEntry != null) {
            nav.popBackStack()
            true
        } else {
            false
        }
    }

    /**
     * Navigate to a new screen with single top mode
     * If the destination screen is already at the top of the stack, it won't create a new instance
     * 
     * @param routeName Route name of the destination screen
     * @param arguments Map of parameters passed via URL query string (optional)
     * @throws IllegalStateException if NavController is not initialized
     * 
     * Example:
     * ```kotlin
     * // Navigate to notification screen, avoiding duplicates
     * pushNamedSingleTop(NotificationView.ROUTER)
     * ```
     */
    @JvmStatic
    fun pushNamedSingleTop(routeName: String, arguments: Map<String, Any>? = null) {
        val nav = navController ?: throw IllegalStateException("NavController not initialized")
        var route = routeName
        arguments?.let { args ->
            val queryParams = args.map { "${it.key}=${it.value}" }.joinToString("&")
            route = "$routeName?$queryParams"
        }
        nav.navigate(route) {
            launchSingleTop = true
        }
    }

    /**
     * Clear the entire navigation stack and navigate to a new screen
     * Equivalent to pushNamedAndRemoveUntil(routeName, predicate = null)
     * 
     * @param routeName Route name of the destination screen
     * @param arguments Map of parameters passed via URL query string (optional)
     * @throws IllegalStateException if NavController is not initialized
     * 
     * Example:
     * ```kotlin
     * // Logout: clear everything and return to login screen
     * clearStackAndPushNamed(LoginView.ROUTER)
     * ```
     */
    @JvmStatic
    fun clearStackAndPushNamed(routeName: String, arguments: Map<String, Any>? = null) {
        pushNamedAndRemoveUntil(routeName, predicate = null, arguments)
    }

    /**
     * Configure the navigation graph for the entire application
     * 
     * This composable function initializes NavHost with default animation effects and
     * calls the builder to define routes in the application.
     * 
     * @param startPage Route of the initial screen when the app opens
     * @param builder Lambda to define routes using NavGraphBuilder
     * 
     * Default animations:
     * - Push: Slide from right to left
     * - Pop: Slide from left to right
     * - Duration: 300ms with FastOutSlowInEasing
     * 
     * Example:
     * ```kotlin
     * @Composable
     * fun MyApp() {
     *     BaseNavigator.ConfigViewNavigator(startPage = HomeView.ROUTER) {
     *         composable(HomeView.ROUTER) {
     *             BaseNavigator.ConfigView<HomeViewModel, HomeView>()
     *         }
     *         composable(DetailView.ROUTER) {
     *             BaseNavigator.ConfigView<DetailViewModel, DetailView>()
     *         }
     *         composable(ProfileView.ROUTER) {
     *             BaseNavigator.ConfigView<ProfileViewModel, ProfileView>()
     *         }
     *     }
     * }
     * ```
     */
    @Composable
    fun ConfigViewNavigator(
        startPage: String,
        builder: NavGraphBuilder.() -> Unit
    ) {
        val navController = rememberNavController()
        this.navController = navController
        NavHost(
            navController = navController,
            startDestination = startPage,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(
                    targetAlpha = 0.8f,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    initialAlpha = 0.8f,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        ) { builder() }
    }

    /**
     * Initialize and configure a screen (View) with its corresponding ViewModel and Overlay
     * 
     * This function automatically:
     * - Creates a View instance using reflection
     * - Injects ViewModel using Hilt
     * - Creates BaseOverlay to manage overlays (dialog, loading, etc.)
     * - Connects View, ViewModel, and Overlay together
     * - Renders View and overlays on the screen
     * 
     * @param VM Generic type of ViewModel, must extend [BaseViewModel]
     * @param V Generic type of View, must extend [BaseView]
     * 
     * Requirements:
     * - ViewModel must be registered with Hilt (@HiltViewModel)
     * - View must have a no-argument constructor
     * - View must extend BaseView<VM>
     * - ViewModel must extend BaseViewModel
     * 
     * Example in NavGraphBuilder:
     * ```kotlin
     * // Define routes in ConfigViewNavigator
     * composable(LoginView.ROUTER) {
     *     ConfigView<LoginViewModel, LoginView>()
     * }
     * 
     * composable(HomeView.ROUTER) {
     *     ConfigView<HomeViewModel, HomeView>()
     * }
     * ```
     * 
     * Example View and ViewModel definition:
     * ```kotlin
     * @HiltViewModel
     * class LoginViewModel @Inject constructor() : BaseViewModel() {
     *     // ViewModel logic
     * }
     * 
     * class LoginView : BaseView<LoginViewModel>() {
     *     companion object {
     *         const val ROUTER = "login"
     *     }
     *     
     *     @Composable
     *     override fun BuildRender() {
     *         // UI composable
     *         Column {
     *             Text("Login Screen")
     *             Button(onClick = {
     *                 pushNamed(HomeView.ROUTER)
     *             }) {
     *                 Text("Login")
     *             }
     *         }
     *     }
     * }
     * ```
     */
    @Composable
    inline fun <reified VM : BaseViewModel, reified V : BaseView<VM>> ConfigView() {
        val view: V = V::class.createInstance()
        val viewModel = hiltViewModel<VM>()
        val overlay = remember { BaseOverlay() }
        view.viewModel = viewModel
        view.overlay = overlay
        viewModel.overlay = overlay
        Box(Modifier.fillMaxSize()) {
            view.BuildRender()
            overlay.overlays.forEach { it() }
        }
    }
}