package com.baott.trackme.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.baott.trackme.R
import com.baott.trackme.helpers.FontCacheHelper

/* 
 * Created by baotran on 10/12/18 
 */

class CustomTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        TextView(context, attrs, defStyleAttr) {

    init {
        applyCustomFont(context, attrs)
    }

    private fun applyCustomFont(context: Context, attrs: AttributeSet?) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
        val font = arr.getInteger(R.styleable.CustomTextView_txtFont, 5)
        val typeface = FontCacheHelper.getTypeface(context, font)
        if (typeface != null) {
            this.typeface = typeface
        }

        // Remove font padding
        includeFontPadding = false
        arr.recycle()
    }
}