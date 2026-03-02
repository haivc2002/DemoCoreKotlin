package com.example.core.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * ## A reusable scrollable list with optional pull-to-refresh.
 *
 * Wraps [LazyColumn] with optional [PullRefreshIndicator].
 *
 * @param modifier Modifier for the outer container.
 * @param padding Content padding for the list (default: 20.dp horizontal & vertical).
 * @param onRefresh Callback for pull-to-refresh. Pass `null` to disable.
 * @param content LazyListScope — use `item {}` and `items() {}` like a normal LazyColumn.
 *
 * ### Usage:
 * ```kotlin
 * // Basic list
 * WidgetListView {
 *     item { Text("Header") }
 *     items(10) { index -> Text("Item $index") }
 * }
 *
 * // With pull-to-refresh
 * WidgetListView(
 *     onRefresh = { viewModel.refresh() }
 * ) {
 *     items(dataList.size) { index ->
 *         Text(dataList[index].name)
 *     }
 * }
 *
 * // Custom padding
 * WidgetListView(
 *     modifier = Modifier.fillMaxSize(),
 *     padding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
 *     onRefresh = { viewModel.refresh() }
 * ) {
 *     items(dataList.size) { index ->
 *         Card { Text("Item $index") }
 *     }
 * }
 * ```
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun WidgetListView(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
    onRefresh: (suspend () -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(refreshing, {
        if (onRefresh != null) {
            scope.launch {
                refreshing = true
                onRefresh()
                refreshing = false
            }
        }
    })

    Box(modifier = modifier) {
        CompositionLocalProvider(
            LocalOverscrollFactory provides null
        ) {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier
                    .fillMaxWidth()
                    .pullRefresh(pullRefreshState),
                content = content
            )
        }
        if (onRefresh != null) {
            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}