package com.example.core.common

import android.util.Log

class PrintLog {
    companion object {
        fun error(message: String, tag: String = "ERROR") {
            Log.e(tag, message)
        }

        fun info(message: String, tag: String = "INFO") {
            Log.i(tag, message)
        }

        fun warning(message: String, tag: String = "WARNING") {
            Log.w(tag, message)
        }
    }
}