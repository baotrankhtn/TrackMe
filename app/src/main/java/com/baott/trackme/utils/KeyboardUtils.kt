package com.baott.trackme.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/*
 * Created by baotran on 1/4/19 
 */

object KeyboardUtils {
    /**
     * Show soft keyboard
     */
    fun showSoftKeyboard(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Hide soft keyboard
     */
    fun hideSoftKeyboard(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0)
    }
}