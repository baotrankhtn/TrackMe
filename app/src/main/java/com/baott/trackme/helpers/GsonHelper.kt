package com.baott.trackme.helpers

import com.google.gson.Gson


/* 
 * Created by baotran on 2020-06-28 
 */

class GsonHelper {
    companion object {
        private var sInstance: Gson

        init {
            sInstance = Gson()
        }

        fun getInstance(): Gson {
            return sInstance
        }
    }
}