package com.baott.trackme.ui.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable


/* 
 * Created by baotran on 2019-07-11 
 */

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected var mCompositeDisposable: CompositeDisposable? = null

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCleared() {
        mCompositeDisposable?.clear()
        super.onCleared()
    }
}