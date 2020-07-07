package com.baott.trackme.network

import com.baott.trackme.entities.network.NewsListResponseEntity
import com.blab.moviestv.network.IApiCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


/* 
 * Created by baotran on 10/4/18 
 */

object ApiManager : IApiManager {
    override fun getNews(disposable: CompositeDisposable?, query: String, callback: IApiCallback<NewsListResponseEntity?, Throwable?>?) {
        disposable?.add(
            RestClient.getInstance().getNews(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t -> callback?.onSuccess(t) }, { t -> callback?.onError(t) }))
    }
}