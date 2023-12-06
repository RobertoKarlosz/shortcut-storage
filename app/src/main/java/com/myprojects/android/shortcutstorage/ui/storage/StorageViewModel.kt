package com.myprojects.android.shortcutstorage.ui.storage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.myprojects.android.shortcutstorage.model.Values
import com.myprojects.android.shortcutstorage.model.storage.StorageDao
import com.myprojects.android.shortcutstorage.model.storage.StorageDatabase
import com.myprojects.android.shortcutstorage.model.storage.StorageItem
import com.myprojects.android.shortcutstorage.model.storage.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StorageViewModel(application: Application) : AndroidViewModel(application) {
    private val storageRepository : StorageRepository
    var items : LiveData<List<StorageItem>>
    var updateItem : StorageItem
    private var sortType: String
    init {
        val storageDao : StorageDao = StorageDatabase.getDatabase(application.applicationContext).storageDao()
        storageRepository = StorageRepository(storageDao)

        items = storageRepository.storageItems
        updateItem = StorageItem(
            id = 0,
            name = "0",
            amount = 0,
            unit = "0")

        sortType = Values.STORAGE_SORT_BY_NAME
    }
    fun add(storageItem : StorageItem) {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepository.insert(storageItem)
        }
    }

    fun updateItem(item : StorageItem) {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepository.update(item)
        }
    }

    fun deleteItem(item : StorageItem) {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepository.delete(item)
        }
    }

    fun deleteAllItem() {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepository.deleteAll()
        }
    }

    fun updateSortType(sortType: String) {
        items = when(sortType) {
            Values.STORAGE_SORT_BY_NAME -> storageRepository.storageItemsByName
            Values.STORAGE_SORT_BY_AMOUNT -> storageRepository.storageItemsByAmount
            Values.STORAGE_SORT_BY_UNIT -> storageRepository.storageItemsByUnit
            else -> storageRepository.storageItems
        }
    }
}