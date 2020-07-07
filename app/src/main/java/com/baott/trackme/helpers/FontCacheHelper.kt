package com.baott.trackme.helpers

import android.content.Context
import android.graphics.Typeface
import androidx.collection.LruCache


/*
 * Created by baotran on 10/12/18
 */

object FontCacheHelper {
    private const val ROBOTO_BLACK = 1
    private const val ROBOTO_BOLD = 2
    private const val ROBOTO_LIGHT = 3
    private const val ROBOTO_MEDIUM = 4
    private const val ROBOTO_REGULAR = 5

    private val sFontCache = LruCache<String, Typeface>(5)

    fun getTypeface(context: Context, font: Int): Typeface? {
        when (font) {
            ROBOTO_BLACK -> return getTypeface(context, "Roboto-Black.ttf")
            ROBOTO_BOLD -> return getTypeface(context, "Roboto-Bold.ttf")
            ROBOTO_LIGHT -> return getTypeface(context, "Roboto-Light.ttf")
            ROBOTO_MEDIUM -> return getTypeface(context, "Roboto-Medium.ttf")
            ROBOTO_REGULAR -> return getTypeface(context, "Roboto-Regular.ttf")
            else -> return getTypeface(context, "Roboto-Light.ttf")
        }
    }

    fun getTypeface(context: Context, fontName: String): Typeface? {
        var typeface: Typeface? = sFontCache.get(fontName)
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.assets, String.format("fonts/%s", fontName))
            } catch (ex: RuntimeException) {
                ex.printStackTrace()
                return null
            }

            sFontCache.put(fontName, typeface!!)
        }
        return typeface
    }
}