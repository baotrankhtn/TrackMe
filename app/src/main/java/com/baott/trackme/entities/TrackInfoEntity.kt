package com.baott.trackme.entities

import android.location.Location


/* 
 * Created by baotran on 2020-07-08 
 */

class TrackInfoEntity {
    var points: MutableList<MyPoint> = ArrayList()
    var duration: Long = 0 // cached to speedup performance (millis)
    var distance: Float = 0f // cached (meter)
    var averageSpeed: Float = 0f // cached (km/h)

    /**
     * Lat, Lng and time it takes to have this point
     */
    class MyPoint(lat: Double, lng: Double) {
        var lat: Double = 0.0
        var lng: Double = 0.0
        var startTime: Long = 0 // millis
        var endTime: Long = 0

        init {
            this.lat = lat
            this.lng = lng
        }

        constructor(lat: Double, lng: Double, startTime: Long, endTime: Long) : this(lat, lng) {
            this.startTime = startTime
            this.endTime = endTime
        }
    }

    fun addPoint(lat: Double, lng: Double, startTime: Long, endTime: Long) {
        // Duration
        duration = duration + endTime - startTime

        // Distance
        if (points.size >= 1) {
            val lastIndex = points.size - 1
            distance += calculateDistance(points[lastIndex].lat, points[lastIndex].lng, lat, lng)
        }

        points.add(MyPoint(lat, lng, startTime, endTime))
    }

    /**
     * Total session distance in meter
     */
    fun calculateDistance(): Float {
        var distance = 0f
        for (index in points.indices - 1) {
            distance += calculateDistance(
                points[index].lat, points[index].lng,
                points[index + 1].lat, points[index + 1].lng
            )
        }
        return distance
    }

    /**
     * Total session duration in milliseconds
     */
    fun calculateDuration(): Long {
        var duration = 0L
        for (point in points) {
            duration = duration + point.endTime - point.startTime
        }
        return duration
    }

    /**
     * Average speed in km/h
     */
    fun calculateAverageSpeed(): Float {
        val distance = calculateDistance() // m
        val duration = calculateDuration() // millis
        return (distance / 1000) / (duration / 3600000)
    }

    /**
     * Current speed based on 3 latest points: km/h
     */
    fun calculateCurrentSpeed(): Float {
        if (points.size >= 3) {
            val lastIndex = points.size - 1
            val secondLastIndex = lastIndex - 1
            val thirdLastIndex = lastIndex - 2
            val distance = calculateDistance(
                points[lastIndex].lat, points[lastIndex].lng,
                points[secondLastIndex].lat, points[secondLastIndex].lng
            ) +
                    calculateDistance(
                        points[secondLastIndex].lat, points[secondLastIndex].lng,
                        points[thirdLastIndex].lat, points[thirdLastIndex].lng
                    )

            val duration =
                (points[lastIndex].endTime - points[lastIndex].startTime)
            +(points[secondLastIndex].endTime - points[secondLastIndex].startTime)
            +(points[thirdLastIndex].endTime - points[thirdLastIndex].startTime)
            return distance
        }
        return calculateAverageSpeed()
    }

    /**
     * Distance in meter between 2 points
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return -1: can not calculate
     */
    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
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

    fun isStartPoint(): Boolean {
        if (points.size == 1) {
            return true
        }
        return false
    }
}