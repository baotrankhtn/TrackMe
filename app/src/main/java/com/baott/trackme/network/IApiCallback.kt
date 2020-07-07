package com.blab.moviestv.network

import com.baott.trackme.entities.network.BaseResponseEntity


/* 
 * Created by baotran on 10/4/18 
 */

interface IApiCallback<D : BaseResponseEntity?, E : Throwable?> {
    fun onSuccess(data: D?)
    fun onError(error: E?)
}