package com.example.cosmostest.utils

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

object Logger {
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        // Ici, utilisez Log.e ou tout autre mécanisme de logging que vous préférez
        println("ERROR: $tag: $message")
        throwable?.printStackTrace()
    }
}
