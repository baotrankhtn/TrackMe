package com.baott.trackme.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baott.trackme.db.RoomManager
import com.baott.trackme.db.callbacks.IGetSessionHistoryCallback
import com.baott.trackme.entities.SessionEntity
import com.baott.trackme.ui.base.BaseViewModel


/* 
 * Created by baotran on 2020-07-09 
 */

class SessionViewModel (application: Application) : BaseViewModel(application) {
    private var mSessionHistory: MutableLiveData<MutableList<SessionEntity>?> = MutableLiveData()

    fun requestSessionHistory(afterId: Long, pageSize: Int) {
        RoomManager.getSessionHistory(getApplication(), afterId, pageSize, object : IGetSessionHistoryCallback {
            override fun onSuccess(sessionHistory: MutableList<SessionEntity>?) {
                mSessionHistory.value = sessionHistory
            }

            override fun onError(throwable: Throwable) {
                mSessionHistory.value = null
            }
        })
    }

    fun getSessionHistory(): LiveData<MutableList<SessionEntity>?> {
        return mSessionHistory
    }
}
