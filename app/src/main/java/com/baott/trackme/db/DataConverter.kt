package com.baott.trackme.db

import androidx.room.TypeConverter
import com.baott.trackme.entities.SessionEntity
import com.baott.trackme.helpers.GsonHelper
import com.google.gson.reflect.TypeToken

/*
 * Created by baotran on 2019-09-05 
 */

class DataConverter {

    @TypeConverter
    fun fromPoints(strEntity: MutableList<SessionEntity.MyPoint>): String {
        val type = object : TypeToken<MutableList<SessionEntity.MyPoint>>() {

        }.type
        return GsonHelper.getInstance().toJson(strEntity, type)
    }

    @TypeConverter
    fun toPoints(strList: String?): MutableList<SessionEntity.MyPoint> {
        val type = object : TypeToken<MutableList<SessionEntity.MyPoint>>() {
        }.type
        return GsonHelper.getInstance().fromJson(strList, type)
    }
}