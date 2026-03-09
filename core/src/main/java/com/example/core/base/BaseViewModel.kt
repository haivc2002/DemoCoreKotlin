package com.example.core.base

import androidx.lifecycle.ViewModel
import com.example.core.common.PrintLog

abstract class BaseViewModel : ViewModel() {
    val overlay: BaseOverlay = BaseNavigator.globalOverlay

    init {
        PrintLog.info("CREATE ${javaClass.simpleName}")
    }
}