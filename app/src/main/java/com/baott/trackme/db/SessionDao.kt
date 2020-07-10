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
    @Query("SELECT * FROM SessionEntity WHERE (id < :afterId) ORDER BY id DESC LIMIT :pageSize")
    fun getSessionHistory(afterId: Long, pageSize: Int): MutableList<SessionEntity>

    @Insert
    fun insert(message: SessionEntity)

    @Delete
    fun delete(message: SessionEntity)
}
