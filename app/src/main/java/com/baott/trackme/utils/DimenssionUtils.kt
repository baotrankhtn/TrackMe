package com.blab.moviestv.utils

import android.content.res.Resources


/*
 * Created by baotran on 10/11/18 
 */

object DimenssionUtils {
    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}