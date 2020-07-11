package com.baott.trackme.db

import android.content.Context
import com.baott.trackme.entities.SessionEntity
import com.baott.trackme.helpers.GsonHelper


/* 
 * Created by baotran on 2020-07-11 
 */

class SharedPreferencesManager {
    object Info {
        const val PREF_NAME = "Prefs"
    }

    object Keys {
        const val SESSION_INFO_TEMP = "SESSION_INFO_TEMP"
    }

    companion object {
        private var sInstance: SharedPreferencesManager

        init {
            sInstance = SharedPreferencesManager()
        }

        fun getInstance(): SharedPreferencesManager {
            return sInstance
        }
    }

    fun setTempSessionInfo(context: Context, sessionInfo: SessionEntity?) {
        val editor = context.getSharedPreferences(Info.PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(Keys.SESSION_INFO_TEMP, GsonHelper.getInstance().toJson(sessionInfo))
        editor.apply()
    }

    fun getTempSessionInfo(context: Context): SessionEntity? {
        val prefs = context.getSharedPreferences(Info.PREF_NAME, Context.MODE_PRIVATE)
        val strConfigs: String? = prefs.getString(Keys.SESSION_INFO_TEMP, null)
        return GsonHelper.getInstance().fromJson(strConfigs, SessionEntity::class.java)
    }
}