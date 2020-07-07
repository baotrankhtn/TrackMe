package com.baott.trackme.utils

import android.content.Context
import android.net.ConnectivityManager
import com.baott.trackme.application.MyApplication


/*
 * Created by baotran on 12/30/18 
 */

object ConnectionUtils {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}