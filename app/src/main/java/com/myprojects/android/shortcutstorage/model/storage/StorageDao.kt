package hu.bme.aut.android.shortcutstorage.model.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StorageDao {
    @Query("SELECT * FROM storage_table ORDER BY id ASC")
    fun getAll(): LiveData<List<StorageItem>>

    @Query("SELECT * FROM storage_table ORDER BY name ASC")
    fun getAllByName(): LiveData<List<StorageItem>>

    @Query("SELECT * FROM storage_table ORDER BY amount ASC")
    fun getAllByAmount(): LiveData<List<StorageItem>>

    @Query("SELECT * FROM storage_table ORDER BY unit ASC")
    fun getAllByUnit(): LiveData<List<StorageItem>>
    @Insert
    suspend fun insert(item : StorageItem)

    @Update
    suspend fun update(item : StorageItem)

    @Delete
    suspend fun delete(item : StorageItem)

    @Query("DELETE FROM storage_table")
    suspend fun deleteAll()
}