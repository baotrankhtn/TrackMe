package com.baott.trackme.entities

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/* 
 * Created by baotran on 2020-07-08 
 */

@Entity
class SessionEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long = 0
    @ColumnInfo(name = "points")
    var points: MutableList<MyPoint> = ArrayList()
    @ColumnInfo(name = "duration")
    var duration: Long = 0 // cached to speedup performance (millis)
    @ColumnInfo(name = "distance")
    var distance: Float = 0f // cached (meter)

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
     * Average speed in km/h
     */
    fun calculateAverageSpeed(): Float {
        if (duration == 0L) {
            return 0f
        }
        return (distance / 1000f) / (duration / 3600000f)
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

            return (distance / 1000f) / (duration / 3600000f)
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
}