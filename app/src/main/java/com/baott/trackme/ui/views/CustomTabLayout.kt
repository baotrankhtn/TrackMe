package com.baott.trackme.ui.views

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.baott.trackme.R
import com.baott.trackme.helpers.FontCacheHelper


/*
 * Created by baotran on 10/18/18 
 */

class CustomTabLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {
    init {
        applyCustomFont(context, attrs)
    }

    private fun applyCustomFont(context: Context, attrs: AttributeSet?) {
        // Get font
        val arr = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
        val font = arr.getInteger(R.styleable.CustomTextView_txtFont, 5)
        val typeface = FontCacheHelper.getTypeface(context, font)

        // Set font
        val vg = getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildsCount = vgTab.childCount
            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = typeface
                }
            }
        }

        arr.recycle()
    }
}