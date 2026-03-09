package com.example.core.base

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class DialogAction(
    val title: String,
    val color: Color = Color(0xFF1947FF),
    val onTap: () -> Unit
)
/**
 * OverlayInterface is a simple manager for displaying transient UI overlays
 * such as dialogs, loading indicators, and snackbars in Jetpack Compose.
 *
 * It uses a MutableStateList to keep track of active overlays. You can add overlays
 * with [dialog], [openLoading], or [snackBar], and remove them with [dismiss] or [dismissAll].
 *
 * Typical usage:
 *  - Wrap your app content with a CompositionLocalProvider to supply an OverlayInterface.
 *  - Collect overlays in a Box and render them on top of your screen.
 *  - Call overlay.dialog { ... } or overlay.snackbar("Hello") anywhere in the UI or ViewModel.
 */
class BaseOverlay {
    private val _overlays = mutableStateListOf<@Composable () -> Unit>()
    val overlays: List<@Composable () -> Unit> get() = _overlays

    /**
     * Show a custom dialog with given content.
     * The dialog is automatically dismissed when calling [dismiss].
     */
    fun dialog(
        body: @Composable () -> Unit,
        title: String = "",
        actions: List<DialogAction> = emptyList()
    ) {
        var wrapper: (@Composable () -> Unit)? = null
        wrapper = {
            Dialog(
                onDismissRequest = {
                    wrapper?.let { _overlays.remove(it) }
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                AppDialog(
                    title = title,
                    body = body,
                    actions = actions,
                    onClose = { wrapper?.let { _overlays.remove(it) } }
                )
            }
        }
        _overlays.add(wrapper)
    }

    /**
     * AppDialog is a custom dialog composable that displays a dialog with a title, body content, and action buttons.
     * 
     * The dialog is designed to:
     * - Fill 90% of the screen width
     * - Constrain maximum width to 450dp
     * - Limit height to 55% of screen height (1/1.8)
     * 
     * This ensures the dialog adapts to screen width on smaller devices,
     * but never exceeds 450dp on larger devices.
     *
     * @param title Optional title text displayed at the top of the dialog
     * @param body Composable content for the dialog body
     * @param actions List of action buttons. If empty, shows a default "Close" button
     * @param onClose Callback invoked when dialog should be dismissed
     */
    @SuppressLint("ConfigurationScreenWidthHeight")
    @Composable
    fun AppDialog(
        title: String = "",
        body: @Composable () -> Unit,
        actions: List<DialogAction> = emptyList(),
        onClose: () -> Unit
    ) {
        val configuration = LocalConfiguration.current
        val targetWidth = configuration.screenWidthDp.dp * 0.9f
        val dialogWidth = if (targetWidth > 450.dp) 450.dp else targetWidth

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier
                .width(dialogWidth)
                .heightIn(max = (configuration.screenHeightDp.dp / 1.8f))
        ) {
            Column(
                modifier = Modifier.padding(top = 15.dp, bottom = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (title.isNotEmpty()) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.85f)
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                ) { body() }
                Spacer(modifier = Modifier.height(8.dp))
                when {
                    actions.isEmpty() -> {
                        TextButton(onClick = onClose) {
                            Text("Đóng", color = Color(0xFF1947FF))
                        }
                    }
                    actions.size <= 2 -> {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 50.dp)
                                .height(IntrinsicSize.Min)
                        ) {
                            actions.forEachIndexed { index, item ->
                                TextButton(
                                    onClick = item.onTap,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = item.title,
                                        color = item.color,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                if (index < actions.lastIndex) {
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(1.dp)
                                            .padding(vertical = 5.dp),
                                        color = Color.Gray.copy(alpha = 0.2f)
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        actions.forEach { item ->
                            TextButton(onClick = item.onTap) {
                                Text(
                                    text = item.title,
                                    color = item.color,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Dismiss the most recently shown overlay.
     */
    fun dismiss() {
        if (_overlays.isEmpty()) return
        _overlays.removeAt(_overlays.lastIndex)
    }

    /**
     * Dismiss all overlays.
     */
    fun dismissAll() = _overlays.clear()

    /**
     * Show a default loading dialog with spinner and text.
     */
    fun openLoading() {
        _overlays.add {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.Black,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

    /**
     * Show a snackbar-like message at the bottom of the screen.
     *
     * @param message The message to display.
     * @param duration How long the snackbar stays visible (ms).
     */
    fun snackBar(message: String, duration: Long = 2000L) {
        val wrapper = @Composable {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    color = Color.Black,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = message,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
        _overlays.add(wrapper)
        CoroutineScope(Dispatchers.Main).launch {
            delay(duration)
            dismiss()
        }
    }
}