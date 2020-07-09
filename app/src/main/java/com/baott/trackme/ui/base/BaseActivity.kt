package com.baott.trackme.ui.base

import com.baott.trackme.R
import com.baott.trackme.ui.dialog.ConfirmDialog
import com.baott.trackme.utils.IntentUtils
import com.blab.moviestv.ui.base.permission.ActivityPermissionManager


/* 
 * Created by baotran on 2019-07-09 
 */

open class BaseActivity : ActivityPermissionManager() {
    private var mDialogConfirm: ConfirmDialog? = null

    protected fun showDialogGps() {
        mDialogConfirm?.dismiss()
        mDialogConfirm =
            ConfirmDialog(this, getString(R.string.dialog_gps_title), object : ConfirmDialog.IOnClickListener {
                override fun onBtnOkClick() {
                    IntentUtils.openGpsSetting(this@BaseActivity)
                }
            })
    }
}