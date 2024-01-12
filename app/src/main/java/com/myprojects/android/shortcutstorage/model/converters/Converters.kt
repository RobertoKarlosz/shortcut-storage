package com.myprojects.android.shortcutstorage.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.myprojects.android.shortcutstorage.model.storage.StorageItem

class Converters {

    @TypeConverter
    fun listToJsonString(value: List<StorageItem>) : String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value : String) = Gson().fromJson(value, Array<StorageItem>::class.java).toList()
}