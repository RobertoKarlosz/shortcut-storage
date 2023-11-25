package hu.bme.aut.android.shortcutstorage.model.storage

import androidx.lifecycle.LiveData

class StorageRepository(private val storageDao : StorageDao) {
    val storageItems : LiveData<List<StorageItem>> = storageDao.getAll()
    val storageItemsByName : LiveData<List<StorageItem>> = storageDao.getAllByName()
    val storageItemsByAmount : LiveData<List<StorageItem>> = storageDao.getAllByAmount()
    val storageItemsByUnit : LiveData<List<StorageItem>> = storageDao.getAllByUnit()

    suspend fun insert(item : StorageItem) {
        storageDao.insert(item)
    }

    suspend fun update(item : StorageItem) {
        storageDao.update(item)
    }

    suspend fun delete(item : StorageItem) {
        storageDao.delete(item)
    }

    suspend fun deleteAll() {
        storageDao.deleteAll()
    }
}