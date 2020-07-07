package com.baott.trackme.utils

import android.content.Context
import android.location.LocationManager
import com.baott.trackme.application.MyApplication


/* 
 * Created by baotran on 2020-07-07 
 */

object LocationUtils {
    fun isGpsAvailable(): Boolean {
        val lm = MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm != null && (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }
}