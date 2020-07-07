package com.baott.trackme.ui.views

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


/* 
 * Created by baotran on 12/31/18 
 */

class CustomViewPager : ViewPager {
    private var paginEnabled: Boolean = false

    constructor(context: Context) : super(context) {
        this.paginEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.paginEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (this.paginEnabled) {
            try {
                return super.onTouchEvent(event)
            } catch (e: Exception) {

            }

        }

        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (this.paginEnabled) {
            try {
                return super.onInterceptTouchEvent(event)
            } catch (e: Exception) {

            }

        }

        return false
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.paginEnabled = enabled
    }
}
