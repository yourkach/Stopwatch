package com.example.stopwatc

import android.util.Log
import kotlinx.coroutines.delay


suspend inline fun startRepeatableAction(
    repeatMillis: Long = 0,
    crossinline action: () -> Boolean
) {
    if (repeatMillis > 0) {
        while (action()) {
            delay(repeatMillis)
        }
    } else {
        action()
    }
}

fun Any.logDebug(message: String) = Log.d(this::class.simpleName, message)