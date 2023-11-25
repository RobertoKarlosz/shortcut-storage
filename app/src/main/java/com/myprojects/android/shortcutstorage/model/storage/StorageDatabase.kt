package hu.bme.aut.android.shortcutstorage.model.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StorageItem::class], version = 1, exportSchema = false)
abstract class StorageDatabase : RoomDatabase() {
    abstract fun storageDao() : StorageDao

    companion object {
        fun getDatabase(applicationContext: Context) : StorageDatabase {
            return Room.databaseBuilder(
                applicationContext,
                StorageDatabase::class.java,
                "storageitems"
            ).build()
        }
    }
}