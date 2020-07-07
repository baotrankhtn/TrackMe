package com.baott.trackme.ui.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.view.View

/*
 * Created by baotran on 12/31/18
 */

open class BaseDialog(context: Context) {
    protected lateinit var mDialog: AlertDialog
    protected var mContext: Context = context
    protected lateinit var mView : View

    protected fun show() {
        mDialog.show()
    }

    protected fun dismiss() {
        if (mDialog.isShowing) {
            mDialog.dismiss()
        }
    }
}
