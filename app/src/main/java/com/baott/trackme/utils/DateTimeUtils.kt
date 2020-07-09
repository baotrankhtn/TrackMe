package com.baott.trackme.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/*
 * Created by baotran on 10/14/18 
 */

object DateTimeUtils {
    const val FORMAT_DATE_TIME: String = "yyyy-MM-dd"
    const val FORMAT_DATE_TIME_US_DETAIL: String = "MMM dd, yyyy"
    const val FORMAT_TIME: String = "%02d:%02d:%02d"

    fun convertMillisToHms(millis: Long): String {
        return String.format(
            FORMAT_TIME,
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }

    fun convertStringToDate(str: String): Calendar {
        val calendar: Calendar = Calendar.getInstance()
        val format = SimpleDateFormat(FORMAT_DATE_TIME)
        try {
            val date: Date = format.parse(str)
            calendar.time = date
        } catch (e: ParseException) {
        }
        return calendar
    }

    fun convertStringToUSDetailDate(str: String): String {
        var result: String = ""
        val calendar: Calendar = Calendar.getInstance()
        val format = SimpleDateFormat(FORMAT_DATE_TIME)
        try {
            val date: Date = format.parse(str)
            calendar.time = date
            val newFormat = SimpleDateFormat(FORMAT_DATE_TIME_US_DETAIL)
            result = newFormat.format(calendar.time)
        } catch (e: ParseException) {
        }
        return result
    }
}