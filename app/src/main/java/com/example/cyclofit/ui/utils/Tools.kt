package com.example.cyclofit.ui.utils

import android.util.Log

object Tools {

    fun debugMessage(message: String, tag: String = "DEBUG_MESSAGE") {
        Log.e(tag, message)
    }
}