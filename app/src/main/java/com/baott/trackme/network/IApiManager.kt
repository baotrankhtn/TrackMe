package com.baott.trackme.network

import com.baott.trackme.entities.network.NewsListResponseEntity
import com.blab.moviestv.network.IApiCallback
import io.reactivex.disposables.CompositeDisposable


/* 
 * Created by baotran on 10/4/18 
 */

interface IApiManager {
    fun getNews(disposable: CompositeDisposable?,
                  query: String,
                  callback: IApiCallback<NewsListResponseEntity?, Throwable?>?)
}