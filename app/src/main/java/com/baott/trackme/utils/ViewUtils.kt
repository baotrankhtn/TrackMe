package com.baott.trackme.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


/*
 * Created by baotran on 10/14/18 
 */

object ViewUtils {
    fun makeFullScreen(activity: Activity) {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    /**
     * Change background color with animation
     */
    fun changeBackgroundColorWithAnimation(view: View, colorFrom: Int, colorTo: Int, duration: Long) {
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