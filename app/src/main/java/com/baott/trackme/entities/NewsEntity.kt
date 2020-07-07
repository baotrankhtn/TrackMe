package com.baott.trackme.entities

import com.google.gson.annotations.SerializedName


/* 
 * Created by baotran on 2019-07-11 
 */

class NewsEntity {
    @SerializedName("title")
    var title: String? = null
    @SerializedName("description")
    var description: String? = null
}