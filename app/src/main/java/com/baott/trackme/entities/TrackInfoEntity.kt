package com.baott.trackme.entities

import android.location.Location


/* 
 * Created by baotran on 2020-07-08 
 */

class TrackInfoEntity {
    var duration: Long = 1 // millis
    var points: MutableList<MyLatLong>? = null

    inner class MyLatLong {
        var lat: Double = 0.0
        var lng: Double = 0.0
    }

    fun calculateDistance(): Float {
        var distance = 0f
        points?.let { valPoints ->
            for (index in valPoints.indices - 1) {
                distance += calculateDistance(
                    valPoints[index].lat, valPoints[index].lng,
                    valPoints[index + 1].lat, valPoints[index + 1].lng
                )
            }
        }
        return distance
    }

    fun calculateAverageSpeed(): Float {
        val distance = calculateDistance() // m
        return (distance / 1000) / (duration / 3600000)
    }

    /**
     * Current speed based on 3 latest points
     */
    fun calculateCurrentSpeed(): Float {
        var distance = 0f
        points?.let { valPoints ->
            if (valPoints.size >= 3) {
                var lastIndex = valPoints.size - 1
                distance = calculateDistance(
                    valPoints[lastIndex].lat, valPoints[lastIndex].lng,
                    valPoints[lastIndex - 1].lat, valPoints[lastIndex - 1].lng
                ) +
                        calculateDistance(
                            valPoints[lastIndex - 1].lat, valPoints[lastIndex - 1].lng,
                            valPoints[lastIndex - 2].lat, valPoints[lastIndex - 2].lng
                        )
            }
        }
        return calculateAverageSpeed()
    }

    /**
     * Distance in meter
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return -1: can not calculate
     */
    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        if (lat1 == 0.0 || lng1 == 0.0 || lat2 == 0.0 || lng2 == 0.0) {
            return 0f
        }

        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = lng1
        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = lng2

        return loc1.distanceTo(loc2)
    }
}