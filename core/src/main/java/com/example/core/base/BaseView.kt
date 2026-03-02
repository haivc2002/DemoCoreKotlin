package com.example.core.base

import androidx.compose.runtime.Composable

abstract class BaseView<VM : BaseViewModel> {
    lateinit var viewModel: VM
    lateinit var overlay: BaseOverlay

    @Composable
    abstract fun BuildRender()
}
