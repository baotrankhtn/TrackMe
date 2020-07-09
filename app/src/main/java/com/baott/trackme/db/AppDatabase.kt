package com.baott.trackme.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.baott.trackme.entities.SessionEntity

/* 
 * Created by baotran on 2019-09-05 
 */

@Database(entities = arrayOf(SessionEntity::class), version = 1)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        private var sIntance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (sIntance == null) {
                synchronized(AppDatabase::class) {
                    sIntance = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "app_room_database.db")
                        .fallbackToDestructiveMigration() // Delete data if error occurs
                        .build()
                }
            }
            return sIntance
        }
    }
}