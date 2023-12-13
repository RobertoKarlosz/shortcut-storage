package com.myprojects.android.shortcutstorage.model.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "storage_table")
data class StorageItem (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "amount") var amount : Int,
    @ColumnInfo(name = "unit") var unit : String
)