package com.baott.trackme.utils

import android.graphics.Bitmap


/* 
 * Created by baotran on 2020-06-28 
 */

object BitmapUtils {
    /**
     * Reduce bitmap to a specified size
     * maxSize can be applied for width or height basing on ratio
     */
    fun resizeBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}