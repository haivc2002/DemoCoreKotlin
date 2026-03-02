package com.example.core.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.api.ApiResult

/**
 * ExceptionWidget (ECWidget) is a composable component that displays error messages
 * with appropriate icons and optional retry functionality.
 * 
 * This widget is commonly used in dialogs or overlay screens to inform users about
 * errors that occurred during API calls or other operations.
 * 
 * The widget displays:
 * - An icon representing the error type
 * - A descriptive error message
 * - An optional "Retry" button if onTap callback is provided
 * 
 * @param errorCode The error code to display. Can be:
 *   - Int: One of the ApiResult error constants (IS_HTTP, IS_TIME_OUT, etc.)
 *   - String: Custom error message to display directly
 *   - null: Displays a generic "Server busy" message
 * @param onTap Optional callback invoked when the retry button is tapped.
 *   If null, no retry button is shown.
 * @param applyTheme Whether to apply themed styling to the error icon (currently unused)
 * 
 * @throws IllegalArgumentException if errorCode is not null, Int, or String
 * 
 * Example usage:
 * ```
 * // Display a network error with retry option
 * ECWidget(
 *     errorCode = ApiResult.IS_NOT_CONNECT,
 *     onTap = { viewModel.retryLogin() }
 * )
 * 
 * // Display a custom error message
 * ECWidget(errorCode = "Invalid credentials")
 * ```
 */
@Composable
fun ECWidget(
    errorCode: Any?,
    onTap: (() -> Unit)? = null,
    applyTheme: Boolean = true
) {
    require(errorCode == null || errorCode is Int || errorCode is String) {
        "errorCode must be Int or String"
    }

    val message = getDefaultMessage(errorCode)

    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ErrorIcon(errorCode, applyTheme)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        if (onTap != null) {
            RetryAction(onTap)
        }
    }
}

/**
 * Maps an error code to a user-friendly Vietnamese error message.
 * 
 * @param errorCode The error code (Int or String)
 * @return Localized error message string. If errorCode is already a String,
 *   returns it directly. Otherwise maps ApiResult constants to Vietnamese messages.
 */
fun getDefaultMessage(errorCode: Any?): String {
    if (errorCode is String) return errorCode
    return when (errorCode) {
        ApiResult.IS_HTTP -> "Lỗi kết nối, vui lòng thử lại"
        ApiResult.IS_TIME_OUT -> "Máy chủ không phản hồi"
        ApiResult.IS_NOT_CONNECT -> "Không có kết nối mạng"
        ApiResult.IS_DUE_SERVER -> "Máy chủ không phản hồi"
        ApiResult.IS_ERROR -> "Đã xảy ra lỗi"
        else -> "Máy chủ bận"
    }
}

/**
 * Displays an icon representing the type of error.
 * 
 * @param errorCode The error code that determines which icon to show
 * @param applyTheme Whether to apply theme styling (currently unused)
 */
@Composable
fun ErrorIcon(errorCode: Any?, applyTheme: Boolean) {
    val icon = when (errorCode) {
        ApiResult.IS_HTTP -> Icons.Default.Error
        ApiResult.IS_TIME_OUT -> Icons.Default.Timer
        ApiResult.IS_NOT_CONNECT -> Icons.Default.Warning
        ApiResult.IS_DUE_SERVER -> Icons.Default.WifiOff
        ApiResult.IS_ERROR -> Icons.Default.Close
        else -> Icons.Default.Info
    }
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(60.dp),
        tint = Color.Gray
    )
}

/**
 * Displays a retry button that invokes the provided callback when tapped.
 * 
 * @param onTap Callback invoked when the retry button is clicked
 */
@Composable
fun RetryAction(onTap: () -> Unit) {
    Spacer(modifier = Modifier.height(12.dp))
    Button(
        onClick = onTap,
        modifier = Modifier.width(120.dp)
    ) {
        Text("Thử lại")
    }
}

/**
 * Utility function that returns only the error message without UI components.
 * 
 * @param errorCode The error code to convert to a message
 * @return The error message string
 */
fun onlyMessage(errorCode: Any?): String {
    return getDefaultMessage(errorCode)
}

/**
 * A simplified error widget that displays only the error message without an icon.
 * 
 * This is useful when you need a more compact error display, such as in smaller
 * dialog boxes or inline error messages.
 * 
 * @param errorCode The error code to display (Int or String)
 * @param onTap Optional callback for retry functionality
 */
@Composable
fun OnlyMessageWidget(
    errorCode: Any?,
    modifier: Modifier = Modifier,
    onTap: (() -> Unit)? = null
) {
    val message = getDefaultMessage(errorCode)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black.copy(alpha = 0.6f)
        )

        if (onTap != null) {
            RetryAction(onTap)
        }
    }
}

