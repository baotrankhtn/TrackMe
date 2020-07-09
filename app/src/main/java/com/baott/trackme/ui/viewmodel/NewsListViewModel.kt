package com.baott.trackme.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baott.trackme.entities.NewsEntity
import com.baott.trackme.entities.network.NewsListResponseEntity
import com.baott.trackme.log.LOG
import com.baott.trackme.network.ApiManager
import com.baott.trackme.ui.base.BaseViewModel
import com.blab.moviestv.network.IApiCallback

/*
 * Created by baotran on 2019-07-11
 */

class NewsListViewModel(application: Application) : BaseViewModel(application) {
    private var mNewsList: MutableLiveData<List<NewsEntity?>?> = MutableLiveData()

    fun requestNewsList(query: String) {
        ApiManager.getNews(mCompositeDisposable, query, object :
            IApiCallback<NewsListResponseEntity?, Throwable?> {
            override fun onSuccess(data: NewsListResponseEntity?) {
                mNewsList.value = data?.articles
                LOG.d("Receiver data")
            }

            override fun onError(error: Throwable?) {
                LOG.d("Error")
            }
        })
    }

    fun getNewsList(): LiveData<List<NewsEntity?>?> {
        return mNewsList
    }
}
