package com.shreeramarchakaru.gayatrijapa.utils

import android.util.Log
import com.shreeramarchakaru.gayatrijapa.BuildConfig

object TraceUtils {
    private const val TAG = "GayatriJapa"

    fun logE(tag: String, message: String) {
        if (BuildConfig.DEBUG_MODE) {
            Log.e("$TAG-$tag", message)
        }
    }

    fun logException(exception: Exception) {
        if (BuildConfig.DEBUG_MODE) {
            Log.e(TAG, "Exception occurred", exception)
            exception.printStackTrace()
        }
    }

    fun logD(tag: String, message: String) {
        if (BuildConfig.DEBUG_MODE) {
            Log.d("$TAG-$tag", message)
        }
    }

    fun logI(tag: String, message: String) {
        if (BuildConfig.DEBUG_MODE) {
            Log.i("$TAG-$tag", message)
        }
    }
}