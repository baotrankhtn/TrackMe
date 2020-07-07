package com.baott.trackme.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.baott.trackme.R
import kotlinx.android.synthetic.main.dialog_confirm.view.*


/* 
 * Created by baotran on 2020-07-07 
 */

class ConfirmDialog(context: Context) : BaseDialog(context) {
    constructor(context: Context, content: String?, listener: IOnClickListener) : this(context) {
        initView(null, content)
        initListener(listener)
    }

    constructor(context: Context, title: String?, content: String?, listener: IOnClickListener) : this(context) {
        initView(title, content)
        initListener(listener)
    }

    private fun initView(title: String?, content: String?) {
        val dialogBuilder = AlertDialog.Builder(mContext)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null)
        dialogBuilder.setView(mView)
        mDialog = dialogBuilder.create()
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        mDialog.setCanceledOnTouchOutside(true)
        mDialog.setCancelable(true)

        if (!title.isNullOrBlank()) {
            mView.mTvTitle.visibility = View.VISIBLE
            mView.mTvTitle.text = title
        } else {
            mView.mTvTitle.visibility = View.GONE
        }

        if (!content.isNullOrBlank()) {
            mView.mTvContent.visibility = View.VISIBLE
            mView.mTvContent.text = content
        } else {
            mView.mTvContent.visibility = View.GONE
        }

        show()
    }

    private fun initListener(listener: IOnClickListener) {
        mView.mBtnOK.setOnClickListener { listener.onBtnOkClick(); dismiss() }

        mView.mBtnCancel.setOnClickListener { dismiss() }
    }

    interface IOnClickListener {
        fun onBtnOkClick()
    }
}
