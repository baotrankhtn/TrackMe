package com.baott.trackme.application

import android.app.Application
import android.content.Context


/* 
 * Created by baotran on 10/5/18 
 */

class MyApplication : Application() {
    init {
        sInstance = this@MyApplication
    }

    companion object {
        private lateinit var sInstance : MyApplication

        fun getContext(): Context {
            return sInstance.applicationContext
        }
    }
}