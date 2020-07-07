package com.baott.trackme.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/* 
 * Created by baotran on 10/14/18 
 */

object DateTimeUtils {
    const val FORMAT_DATE_TIME: String = "yyyy-MM-dd"
    const val FORMAT_DATE_TIME_US_DETAIL: String = "MMM dd, yyyy"

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