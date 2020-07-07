package com.baott.trackme.entities.network

import com.google.gson.annotations.SerializedName


/* 
 * Created by baotran on 10/5/18 
 */

open class BaseResponseEntity {
    @SerializedName("status")
    var status : String? = null
    @SerializedName("totalResults")
    var totalResults : Long? = 0
}

