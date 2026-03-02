package com.example.core.base

import androidx.lifecycle.ViewModel
import com.example.core.common.PrintLog

abstract class BaseViewModel : ViewModel() {
    lateinit var overlay: BaseOverlay

    init {
        PrintLog.info("CREATE ${javaClass.simpleName}")
    }
}