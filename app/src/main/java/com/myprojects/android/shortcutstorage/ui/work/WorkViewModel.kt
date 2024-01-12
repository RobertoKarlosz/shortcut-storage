package com.myprojects.android.shortcutstorage.ui.work

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.myprojects.android.shortcutstorage.model.Values
import com.myprojects.android.shortcutstorage.model.work.Work
import com.myprojects.android.shortcutstorage.model.work.WorkDao
import com.myprojects.android.shortcutstorage.model.work.WorkDatabase
import com.myprojects.android.shortcutstorage.model.work.WorkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkViewModel(application: Application) : AndroidViewModel(application) {
    private val workRepository : WorkRepository
    var works : LiveData<List<Work>>
    val activeWorks: LiveData<List<Work>>
    var updateWork : Work
    private var sortType: String

    init {
        val workDao : WorkDao = WorkDatabase.getDatabase(application.applicationContext).workDao()
        workRepository = WorkRepository(workDao)
        works = workRepository.works
        activeWorks = workRepository.activeWorks
        updateWork = Work(
            id = 0,
            name = "",
            date = "",
            address = "",
            description = "",
            materials = emptyList()
        )
        sortType = Values.WORK_SORT_BY_NAME
    }

    fun add(work : Work) {
        viewModelScope.launch(Dispatchers.IO) {
            workRepository.insert(work)
        }
    }

    fun updateWork(work : Work) {
        viewModelScope.launch(Dispatchers.IO) {
            workRepository.update(work)
        }
    }

    fun deleteWork(work : Work) {
        viewModelScope.launch(Dispatchers.IO) {
            workRepository.delete(work)
        }
    }

    fun deleteAllWork() {
        viewModelScope.launch(Dispatchers.IO) {
            workRepository.deleteAll()
        }
    }

    fun updateSortType(sortType: String) {
        works = when(sortType) {
            Values.WORK_SORT_BY_NAME -> workRepository.worksByName
            Values.WORK_SORT_BY_ADDRESS -> workRepository.worksByAddress
            Values.WORK_SORT_BY_DATE -> workRepository.worksByDate
            else -> workRepository.works
        }
    }
}