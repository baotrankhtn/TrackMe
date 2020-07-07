package com.baott.trackme.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle


/* 
 * Created by baotran on 11/6/18 
 */

object IntentUtils {
    /**
     * Open normal activity
     */
    fun openActivity(context: Context, clazz: Class<*>, bundle: Bundle?, finishCurrent: Boolean) {
        val intent = Intent(context, clazz)
        bundle?.let {
            intent.putExtras(it)
        }
        context.startActivity(intent)

        if (finishCurrent && context is Activity) {
            context.finish()
        }
    }
}