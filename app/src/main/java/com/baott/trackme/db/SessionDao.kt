package com.baott.trackme.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.baott.trackme.entities.SessionEntity

/*
 * Created by baotran on 2019-09-05
 */

@Dao
interface SessionDao {
    @Query("SELECT * FROM SessionEntity LIMIT :pageSize")
    fun getSessionHistory(pageSize: Int): MutableList<SessionEntity>

    //
//    @Query("SELECT * FROM ChatContentEntity WHERE mid < :mId ORDER BY mid DESC LIMIT 20")
//    fun getPreviousMessagesExcludeCurrentMessage(mId: String): MutableList<ChatContentEntity>
//
    @Insert
    fun insert(message: SessionEntity)

    @Delete
    fun delete(message: SessionEntity)
}
