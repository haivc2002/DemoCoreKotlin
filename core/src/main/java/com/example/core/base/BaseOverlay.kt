package com.example.core.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * OverlayInterface is a simple manager for displaying transient UI overlays
 * such as dialogs, loading indicators, and snackbars in Jetpack Compose.
 *
 * It uses a MutableStateList to keep track of active overlays. You can add overlays
 * with [dialog], [dialogLoading], or [snackBar], and remove them with [dismiss] or [dismissAll].
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
//    fun dialog(content: @Composable () -> Unit) {
//        var wrapper: (@Composable () -> Unit)? = null
//        wrapper = {
//            Dialog(onDismissRequest = {
//                wrapper?.let { dismiss() }
//            }) {
//                content()
//            }
//        }
//        _overlays.add(wrapper)
//    }

    /**
     * Dismiss the most recently shown overlay.
     */
    fun dismiss() {
        if (_overlays.isNotEmpty()) _overlays.removeAt(_overlays.size - 1)
    }

    /**
     * Dismiss all overlays.
     */
    fun dismissAll() = _overlays.clear()

    /**
     * Show a default loading dialog with spinner and text.
     */
//    fun dialogLoading() = dialog {
//        Surface(
//            color = Color.Black,
//            shape = RoundedCornerShape(20.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(all = 15.dp).padding(top = 10.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                CircularProgressIndicator(
//                    color = Color.White,
//                    modifier = Modifier.size(24.dp)
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    "Xin chờ",
//                    style = TextStyle(color = Color.White)
//                )
//            }
//        }
//    }

    /**
     * Show a snackbar-like message at the bottom of the screen.
     *
     * @param message The message to display.
     * @param duration How long the snackbar stays visible (ms).
     */
//    fun snackBar(message: String, duration: Long = 2000L) {
//        var wrapper: (@Composable () -> Unit)? = null
//        wrapper = {
//            Box(
//                Modifier
//                    .fillMaxSize()
//                    .padding(bottom = 40.dp),
//                contentAlignment = Alignment.BottomCenter
//            ) {
//                Surface(
//                    color = Color.Black,
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text(
//                        text = message,
//                        color = Color.White,
//                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                    )
//                }
//            }
//        }
//        _overlays.add(wrapper)
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(duration)
//            dismiss()
//        }
//    }

    /**
     * CompositionLocal provider for OverlayInterface.
     * Must be provided at the root of your composable tree.
     */
    val localOverlayManager = staticCompositionLocalOf<BaseOverlay> {
        error("No OverlayManager provided")
    }
}