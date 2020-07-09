package com.baott.trackme.db.callbacks

import com.baott.trackme.entities.SessionEntity


/* 
 * Created by baotran on 2020-07-09 
 */

interface IGetSessionHistoryCallback {
    fun onSuccess(sessionHistory: MutableList<SessionEntity>?)
    fun onError(throwable: Throwable)
}