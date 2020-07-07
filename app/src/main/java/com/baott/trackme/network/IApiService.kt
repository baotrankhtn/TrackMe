package com.baott.trackme.network

import com.baott.trackme.entities.network.NewsListResponseEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


/* 
 * Created by baotran on 10/4/18 
 */

interface IApiService {
    @GET("everything")
    fun getNews(@Query("q") query: String): Observable<NewsListResponseEntity>

}