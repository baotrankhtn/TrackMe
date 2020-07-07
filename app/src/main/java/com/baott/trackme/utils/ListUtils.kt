package com.baott.trackme.utils


/* 
 * Created by baotran on 10/9/18 
 */

object ListUtils {
    fun <T : Any?> isNullOrEmpty(list: MutableList<T>?): Boolean {
        if (list != null && list.isNotEmpty()) {
            return false
        }
        return true
    }
}