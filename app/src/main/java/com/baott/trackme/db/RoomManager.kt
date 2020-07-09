package com.baott.trackme.db

import android.annotation.SuppressLint
import android.content.Context
import com.baott.trackme.db.callbacks.IInsertSessionCallback
import com.baott.trackme.entities.SessionEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/* 
 * Created by baotran on 2019-09-05 
 */

object RoomManager {
//    @SuppressLint("CheckResult")
//    fun getPreviousChatMessages(
//        context: Context,
//        mId: String,
//        includeCurrentMessage: Boolean,
//        callback: IOnRoomChatMessagesListener?
//    ) {
//        if (includeCurrentMessage) {
//            Observable.fromCallable {
//                AppDatabase.getInstance(context)?.sessionDao()?.getPreviousMessagesIncludeCurrentMessage(mId)
//            }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    it?.reverse()
//                    callback?.onSuccess(it!!)
//                }, {
//                    callback?.onError(it)
//                })
//        } else {
//            Observable.fromCallable {
//                AppDatabase.getInstance(context)?.sessionDao()?.getPreviousMessagesExcludeCurrentMessage(mId)
//            }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    callback?.onSuccess(it!!)
//                }, {
//                    callback?.onError(it)
//                })
//        }
//    }
//
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