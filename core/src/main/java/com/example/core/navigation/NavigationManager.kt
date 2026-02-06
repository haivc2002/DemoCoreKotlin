package com.example.core.navigation

import androidx.navigation.NavHostController
import javax.inject.Inject
import javax.inject.Singleton

/**
 * NavigationManager - Singleton service quản lý navigation.
 * Thay thế cho BaseNavigator object với static field để tránh memory leak.
 * 
 * Sử dụng Hilt để inject vào các ViewModels hoặc component khác.
 * 
 * Example:
 * ```
 * @HiltViewModel
 * class MyViewModel @Inject constructor(
 *     private val navigationManager: NavigationManager
 * ) : BaseViewModel() {
 *     fun navigateToDetail() {
 *         navigationManager.pushNamed("detail_route")
 *     }
 * }
 * ```
 */
@Singleton
class NavigationManager @Inject constructor() {
    
    private var _navController: NavHostController? = null
    
    /**
     * Inject NavController từ Composable root.
     * Nên được gọi từ BaseNavigator.BaseNavigation()
     */
    fun setNavController(controller: NavHostController) {
        _navController = controller
    }
    
    /**
     * Navigate đến route mới.
     * 
     * @param routeName Tên route đã định nghĩa trong NavHost
     * @param arguments Optional query parameters
     */
    fun pushNamed(routeName: String, arguments: Map<String, Any>? = null) {
        val nav = requireNavController()
        var route = routeName
        arguments?.let { args ->
            val queryParams = args.map { "${it.key}=${it.value}" }.joinToString("&")
            route = "$routeName?$queryParams"
        }
        nav.navigate(route)
    }
    
    /**
     * Navigate đến route mới và replace màn hình hiện tại.
     */
    fun pushReplacementNamed(routeName: String, arguments: Map<String, Any>? = null) {
        val nav = requireNavController()
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
     * Navigate và xóa back stack đến route chỉ định.
     * 
     * @param routeName Route đích
     * @param predicate Route để popUpTo, null = clear toàn bộ stack
     * @param arguments Optional query parameters
     */
    fun pushNamedAndRemoveUntil(
        routeName: String,
        predicate: String? = null,
        arguments: Map<String, Any>? = null
    ) {
        val nav = requireNavController()
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
     * Pop màn hình hiện tại và navigate đến route mới.
     */
    fun popAndPushNamed(routeName: String, arguments: Map<String, Any>? = null) {
        val nav = requireNavController()
        nav.popBackStack()
        pushNamed(routeName, arguments)
    }
    
    /**
     * Quay lại màn hình trước đó.
     * 
     * @param result Optional result data (hiện tại chưa implement)
     */
    fun pop(result: Map<String, Any>? = null) {
        val nav = requireNavController()
        nav.popBackStack()
    }
    
    /**
     * Pop back stack đến route chỉ định.
     */
    fun popUntil(routeName: String) {
        val nav = requireNavController()
        nav.popBackStack(routeName, inclusive = false)
    }
    
    /**
     * Kiểm tra có thể pop hay không.
     */
    fun canPop(): Boolean {
        val nav = _navController ?: return false
        return nav.previousBackStackEntry != null
    }
    
    /**
     * Pop nếu có thể, trả về true nếu đã pop.
     */
    fun maybePop(): Boolean {
        val nav = _navController ?: return false
        return if (nav.previousBackStackEntry != null) {
            nav.popBackStack()
            true
        } else {
            false
        }
    }
    
    /**
     * Navigate với launchSingleTop = true.
     */
    fun pushNamedSingleTop(routeName: String, arguments: Map<String, Any>? = null) {
        val nav = requireNavController()
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
     * Clear toàn bộ back stack và navigate đến route mới.
     */
    fun clearStackAndPushNamed(routeName: String, arguments: Map<String, Any>? = null) {
        pushNamedAndRemoveUntil(routeName, predicate = null, arguments)
    }
    
    /**
     * Helper để lấy NavController với error message rõ ràng.
     */
    private fun requireNavController(): NavHostController {
        return _navController ?: throw IllegalStateException(
            "NavController chưa được khởi tạo. " +
            "Hãy đảm bảo BaseNavigator.BaseNavigation() đã được gọi."
        )
    }
}
