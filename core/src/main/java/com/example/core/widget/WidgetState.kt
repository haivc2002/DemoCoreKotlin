package com.example.core.widget

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable

/**
 * A generic UI handler for displaying different UI states (Loading, Success, Error).
 *
 * @param state The current state of the API request (Loading, Success, or Error).
 * @param onLoading UI to show while loading (default: CircularProgressIndicator).
 * @param onSuccess UI to show when the request succeeds, with the returned data.
 * @param onError UI to show when the request fails, with the error code.
 *
 * Example:
 * ```kotlin
 * WidgetState(
 *     state = loginState,
 *     onSuccess = { data -> Text("Welcome ${data.username}") },
 *     onError = { code -> Text("Error code: $code", color = Color.Red) }
 * )
 * ```
 */
@Composable
fun <T> WidgetState(
    state: StateApi<T>,
    onLoading: @Composable () -> Unit = { CircularProgressIndicator() },
    onSuccess: @Composable (T) -> Unit,
    onError: @Composable (Any) -> Unit
) {
    when (state) {
        is StateApi.Failure -> onError(state.coreOrMes)
        StateApi.Loading -> onLoading()
        is StateApi.Success -> onSuccess(state.data)
    }
}

/**
 * Represents the state of an API request.
 *
 * This sealed-like hierarchy is used to drive UI rendering in Compose.
 *
 * Typical flow:
 * Loading → Success(data)
 * Loading → Failure(code, message)
 *
 * @param T The type of data returned on success.
 */
open class StateApi<out T> {

    /**
     * Indicates that the request is currently in progress.
     */
    data object Loading : StateApi<Nothing>()

    /**
     * Indicates that the request completed successfully.
     *
     * @param data The data returned from the API.
     */
    data class Success<T>(
        val data: T
    ) : StateApi<T>()

    /**
     * Indicates that the request failed.
     *
     * @param code Error code (usually HTTP status code or custom error code).
     * @param message Optional error message for debugging or display.
     */
    data class Failure(
        val coreOrMes: Any
    ) : StateApi<Nothing>()
}

