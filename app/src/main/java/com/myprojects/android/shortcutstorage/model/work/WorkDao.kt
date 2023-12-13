package com.myprojects.android.shortcutstorage.model.work

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkDao {

    @Query("SELECT * FROM works_table ORDER BY done ASC, id ASC")
    fun getAll() : LiveData<List<Work>>

    @Query("SELECT * FROM works_table ORDER BY done ASC, name ASC")
    fun getAllByName() : LiveData<List<Work>>

    @Query("SELECT * FROM works_table ORDER BY done ASC, address ASC")
    fun getAllByAddress() : LiveData<List<Work>>

    @Query("SELECT * FROM works_table ORDER BY done ASC, date DESC")
    fun getAllByDate() : LiveData<List<Work>>

    @Query("SELECT * FROM works_table WHERE done = 0 ORDER BY date DESC LIMIT 1")
    fun getAllActive() : LiveData<List<Work>>

    @Insert
    suspend fun insert(work : Work)

    @Update
    suspend fun update(work: Work)

    @Delete
    suspend fun delete(work : Work)

    @Query("DELETE FROM works_table")
    suspend fun deleteAll()
}