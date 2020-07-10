package com.baott.trackme.db

import android.annotation.SuppressLint
import android.content.Context
import com.baott.trackme.db.callbacks.IGetSessionHistoryCallback
import com.baott.trackme.db.callbacks.IInsertSessionCallback
import com.baott.trackme.entities.SessionEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/* 
 * Created by baotran on 2019-09-05 
 */

object RoomManager {
    @SuppressLint("CheckResult")
    fun getSessionHistory(context: Context, afterId: Long, pageSize: Int, callback: IGetSessionHistoryCallback?) {
        Observable.fromCallable {
            AppDatabase.getInstance(context)?.sessionDao()?.getSessionHistory(afterId, pageSize)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback?.onSuccess(it)
            }, {
                callback?.onError(it)
            })
    }

    @SuppressLint("CheckResult")
    fun insertSession(context: Context, entity: SessionEntity, callback: IInsertSessionCallback?) {
        Observable.fromCallable { AppDatabase.getInstance(context)?.sessionDao()?.insert(entity) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback?.onSuccess(entity)
            }, {
                callback?.onError(it)
            })
    }
}