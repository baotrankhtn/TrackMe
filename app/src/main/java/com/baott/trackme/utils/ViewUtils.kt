package com.baott.trackme.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.WindowManager


/*
 * Created by baotran on 10/14/18 
 */

object ViewUtils {
    /**
     * Change background color with animation
     */
    fun  changeBackgroundColorWithAnimation(view : View, colorFrom : Int, colorTo : Int, duration : Long) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = duration // milliseconds
        colorAnimation.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()
    }

    /**
     * Set status bar color from Android 5
     */
    fun setStatusBarColor(activity: Activity, colorInt: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = colorInt
        }
    }

    /**
     * Find fragment
     */
    fun findFragment(activity: FragmentActivity, fragmentClass: Class<*>): Fragment? {
        val fragments = activity.supportFragmentManager.fragments
        if (fragments != null) {
            for (i in fragments.indices) {
                if (fragments[i].javaClass == fragmentClass) {
                    return fragments[i]
                }
            }
        }
        return null
    }
}