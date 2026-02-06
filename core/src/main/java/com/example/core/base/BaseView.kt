package com.example.core.base

import androidx.compose.runtime.Composable

abstract class BaseView<VM : BaseViewModel> {

    lateinit var viewModel: VM

    @Composable
    fun Render() {
        buildRender()
    }

    @Composable
    protected abstract fun buildRender()
}
