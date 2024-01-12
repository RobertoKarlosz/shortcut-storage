package com.myprojects.android.shortcutstorage.model.work

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myprojects.android.shortcutstorage.model.converters.Converters

@Database(entities = [Work::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WorkDatabase : RoomDatabase() {
    abstract fun workDao() : WorkDao

    companion object {
        fun getDatabase(applicationContext: Context) : WorkDatabase {
            return Room.databaseBuilder(
                applicationContext,
                WorkDatabase::class.java,
                "works"
            ).build()
        }
    }
}