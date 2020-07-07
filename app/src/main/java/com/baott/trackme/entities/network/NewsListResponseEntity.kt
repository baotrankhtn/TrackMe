package com.baott.trackme.entities.network

import com.baott.trackme.entities.NewsEntity
import com.google.gson.annotations.SerializedName


/* 
 * Created by baotran on 2019-07-11 
 */

class NewsListResponseEntity : BaseResponseEntity() {
    @SerializedName("articles")
    var articles: MutableList<NewsEntity?>? = null
}