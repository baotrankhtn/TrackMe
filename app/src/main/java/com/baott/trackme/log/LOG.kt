package com.baott.trackme.log

import android.util.Log
import com.baott.trackme.BuildConfig


/* 
 * Created by baotran on 10/7/18 
 */

object LOG {
    private val isLogEnabled = !BuildConfig.BUILD_TYPE.equals("release", ignoreCase = true)
    private val TAG = ">>>"

    fun v(message: String) {
        if (isLogEnabled) {
            Log.v(TAG, message)
        }
    }

    fun v(tag: String, message: String) {
        if (isLogEnabled) {
            Log.v(tag, message)
        }
    }

    fun d(message: String) {
        if (isLogEnabled) {
            Log.d(TAG, message)
        }
    }

    fun d(tag: String, message: String) {
        if (isLogEnabled) {
            Log.d(tag, message)
        }
    }
}